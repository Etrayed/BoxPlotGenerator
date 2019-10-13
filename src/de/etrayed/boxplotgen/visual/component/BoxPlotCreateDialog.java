package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.Callback;
import de.etrayed.boxplotgen.visual.Window;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

/**
 * @author Etrayed
 */
class BoxPlotCreateDialog extends JDialog {

    static final int RAW = 1;
    static final int CALCULATED = 2;

    BoxPlotCreateDialog(Frame owner, int type, Callback<BoxPlotInfo> callback) {
        super(owner);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setTitle("Neues BoxPlot erstellen");
        setIconImage(Window.ICON);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setBounds(Window.center(screenSize.width, 800), Window.center(screenSize.height, 600), 800, 600);

        try {
            Callable<BoxPlotInfo> infoCallable;

            if(type == RAW) {
                infoCallable = setupRaw();
            } else if(type == CALCULATED) {
                infoCallable = setupCalculated();
            } else {
                throw new IllegalArgumentException("Invalid type: " + type);
            }

            JButton confirmButton = new JButton("Erstellen");

            confirmButton.setBounds(Window.center(getWidth(), 150), 450, 150, 40);
            confirmButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        BoxPlotInfo result = infoCallable.call(); // NumberFormatException gets thrown if necessary

                        setVisible(false);

                        callback.handleSuccess(infoCallable.call());
                    } catch (NumberFormatException unused) {
                        Toolkit.getDefaultToolkit().beep();

                        JOptionPane.showMessageDialog(getParent(), "Einer der angegebenen Werte ist keine Zahl!");
                    } catch (IllegalArgumentException ex) {
                        Toolkit.getDefaultToolkit().beep();

                        JOptionPane.showMessageDialog(getParent(), "Einer der angegebenen Werte ist ungültig! ("
                                + ex.getMessage() + ")");
                    } catch (Exception ex) {
                        throw new Error(ex);
                    }
                }
            });

            add(confirmButton);
        } catch (Exception e) {
            callback.handleException(e);
        }
    }

    private Callable<BoxPlotInfo> setupRaw() {
        JTextField nameField = new JTextField("Name...");
        JTextField valuesField = new JTextField("Werte...");

        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new LimitedDocumentFilter(25));

        nameField.setBounds(Window.center(getWidth(), 250), 150, 250, 40);
        nameField.setToolTipText("Name des BoxPlot's");

        valuesField.setBounds(Window.center(getWidth(), 350), 220, 350, 40);
        valuesField.setToolTipText("Werte mit einem Semikolon getrennt");

        add(nameField);
        add(valuesField);

        return new Callable<BoxPlotInfo>() {

            @Override
            public BoxPlotInfo call() {
                return BoxPlotInfo.createNew(nameField.getText(), valuesFromString(valuesField.getText()));
            }
        };
    }

    private double[] valuesFromString(String text) {
        String[] rawValues = text.split(";");
        double[] values = new double[rawValues.length];

        for (int i = 0; i < rawValues.length; i++) {
            values[i] = Double.parseDouble(rawValues[i]);
        }

        return values;
    }

    private Callable<BoxPlotInfo> setupCalculated() {
        JTextField nameField = new JTextField("Name...");
        JSpinner minimumSpinner = new JSpinner(new SpinnerNumberModel(0.0D, null,
                null, 1.0D));
        JSpinner lowerQuartileSpinner = new JSpinner(new SpinnerNumberModel(0.0D, null,
                null, 1.0D));
        JSpinner medianSpinner = new JSpinner(new SpinnerNumberModel(0.0D, null,
                null, 1.0D));
        JSpinner upperQuartileSpinner = new JSpinner(new SpinnerNumberModel(0.0D, null,
                null, 1.0D));
        JSpinner maximumSpinner = new JSpinner(new SpinnerNumberModel(0.0D, null,
                null, 1.0D));

        ((AbstractDocument) nameField.getDocument()).setDocumentFilter(new LimitedDocumentFilter(25));

        nameField.setBounds(Window.center(getWidth(), 250), 150, 250, 40);
        nameField.setToolTipText("Name des BoxPlot's");

        minimumSpinner.setBounds(Window.center(getWidth(), 175), 220, 75, 40);
        minimumSpinner.setToolTipText("Minimaler Wert");

        lowerQuartileSpinner.setBounds(minimumSpinner.getX() + 100, 220, 75, 40);
        lowerQuartileSpinner.setToolTipText("Unteres Quartil");

        medianSpinner.setBounds(Window.center(getWidth(), 75), 290, 75, 40);
        medianSpinner.setToolTipText("Median");

        upperQuartileSpinner.setBounds(Window.center(getWidth(), 175), 360, 75, 40);
        upperQuartileSpinner.setToolTipText("Oberes Quartil");

        maximumSpinner.setBounds(upperQuartileSpinner.getX() + 100, 360, 75, 40);
        maximumSpinner.setToolTipText("Maximaler Wert");

        add(nameField);
        add(minimumSpinner);
        add(lowerQuartileSpinner);
        add(medianSpinner);
        add(upperQuartileSpinner);
        add(maximumSpinner);

        return new Callable<BoxPlotInfo>() {

            @Override
            public BoxPlotInfo call() {
                return BoxPlotInfo.createNew(nameField.getText(), ((Number) minimumSpinner.getValue()).doubleValue(),
                        ((Number) lowerQuartileSpinner.getValue()).doubleValue(),
                        ((Number) medianSpinner.getValue()).doubleValue(),
                        ((Number) upperQuartileSpinner.getValue()).doubleValue(),
                        ((Number) maximumSpinner.getValue()).doubleValue());
            }
        };
    }

    private static final class LimitedDocumentFilter extends DocumentFilter {

        private final int limit;

        private LimitedDocumentFilter(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            replace(fb, offset, 0, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            int redundant = (fb.getDocument().getLength() + text.length()) - limit - length;

            if(redundant > 0) {
                text = text.substring(0, text.length() - redundant);
            }

            if(text.length() == 0) {
                Toolkit.getDefaultToolkit().beep();

                return;
            }

            fb.replace(offset, length, text, attrs);
        }
    }
}
