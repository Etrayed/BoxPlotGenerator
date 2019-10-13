package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.BoxPlotGenerator;
import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.Callback;
import de.etrayed.boxplotgen.visual.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Predicate;

/**
 * @author Etrayed
 */
public class BPGMenuBar extends JMenuBar {

    private static final Callback<BoxPlotInfo> CREATE_CALLBACK;

    static {
        CREATE_CALLBACK = new Callback<BoxPlotInfo>() {

            @Override
            public void handleSuccess(BoxPlotInfo info) {
                BoxPlotGenerator.getInstance().getBoxPlotInfoList().add(info);
                BoxPlotGenerator.getInstance().repaintCanvas();
            }

            @Override
            public void handleException(Exception e) {
                e.printStackTrace();
            }
        };
    }

    private final JDialog scalingDialog, deleteDialog;

    private final JFileChooser fileChooser;

    private final BoxPlotCreateDialog rawCreateDialog, calculatedCreateDialog;

    private JSpinner scalingSpinner;

    private JComboBox<String> boxPlotChooser;

    public BPGMenuBar(JFrame parent) {
        this.scalingDialog = new JDialog(parent, "Skalierung");
        this.deleteDialog = new JDialog(parent, "Löschen");
        this.fileChooser = new JFileChooser();
        this.rawCreateDialog = new BoxPlotCreateDialog(parent, BoxPlotCreateDialog.RAW, CREATE_CALLBACK);
        this.calculatedCreateDialog = new BoxPlotCreateDialog(parent, BoxPlotCreateDialog.CALCULATED, CREATE_CALLBACK);

        setupScalingDialog(parent);
        setupDeleteDialog(parent);
        setupFileChooser();
        setupNewMenu();
        setupManageMenu();
    }

    private void setupScalingDialog(JFrame parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        scalingDialog.setBounds(Window.center(screenSize.width, 156), Window.center(screenSize.height, 79),
                156, 79);
        scalingDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        scalingDialog.setResizable(false);
        scalingDialog.setLayout(null);
        scalingDialog.setIconImage(Window.ICON);

        this.scalingSpinner = new JSpinner(new SpinnerNumberModel(1, 1, null, 1));

        scalingSpinner.setSize(150, 50);
        scalingSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                BoxPlotGenerator.getInstance().setGlobalScaling((Integer) scalingSpinner.getValue());
                BoxPlotGenerator.getInstance().repaintCanvas();
            }
        });

        scalingDialog.add(scalingSpinner);
    }

    private void setupDeleteDialog(JFrame parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        deleteDialog.setBounds(Window.center(screenSize.width, 198), Window.center(screenSize.height, 69),
                198, 69);
        deleteDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        deleteDialog.setResizable(false);
        deleteDialog.setLayout(null);
        deleteDialog.setIconImage(Window.ICON);

        boxPlotChooser = new JComboBox<>();
        boxPlotChooser.setBounds(0, 0, 132, 40);

        JButton confirmButton = new JButton("✔");

        confirmButton.setBounds(142, 0, 50, 40);
        confirmButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BoxPlotGenerator.getInstance().getBoxPlotInfoList().removeIf(new Predicate<BoxPlotInfo>() {

                    @Override
                    public boolean test(BoxPlotInfo info) {
                        return info.name.equals(boxPlotChooser.getSelectedItem());
                    }
                });
                BoxPlotGenerator.getInstance().repaintCanvas();

                deleteDialog.setVisible(false);
            }
        });

        deleteDialog.add(boxPlotChooser);
        deleteDialog.add(confirmButton);
    }

    private void setupFileChooser() {
        fileChooser.resetChoosableFileFilters();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setDialogTitle("Speicherort auswählen");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Portable Network Graphics (PNG, png)",
                "PNG", "png");

        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
    }

    private void setupNewMenu() {
        JMenu newMenu = new JMenu("  Neu  ");

        JMenuItem newRawItem = new JMenuItem("Rohe Werte");
        JMenuItem newCalculatedItem = new JMenuItem("Ausgerechnete Werte");

        newRawItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(BoxPlotGenerator.getInstance().getBoxPlotInfoList().size() >= 12) {
                    JOptionPane.showMessageDialog(getParent(), "Es nur 12 BoxPlots gleichzeitig erlaubt!");

                    Toolkit.getDefaultToolkit().beep();

                    return;
                }

                rawCreateDialog.setVisible(true);
            }
        });

        newCalculatedItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(BoxPlotGenerator.getInstance().getBoxPlotInfoList().size() >= 12) {
                    JOptionPane.showMessageDialog(getParent(), "Es nur 12 BoxPlots gleichzeitig erlaubt!");

                    Toolkit.getDefaultToolkit().beep();

                    return;
                }

                calculatedCreateDialog.setVisible(true);
            }
        });

        newMenu.add(newRawItem);
        newMenu.addSeparator();
        newMenu.add(newCalculatedItem);

        this.add(newMenu);
    }

    private void setupManageMenu() {
        JMenu manageMenu = new JMenu("  Verwalten  ");
        JMenuItem removeItem = new JMenuItem("Entfernen");
        JMenuItem exportItem = new JMenuItem("Exportieren");
        JMenuItem scalingItem = new JMenuItem("Skalierung");

        removeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(BoxPlotGenerator.getInstance().getBoxPlotInfoList().size() <= 1) {
                    BoxPlotGenerator.getInstance().getBoxPlotInfoList().clear();

                    return;
                }

                boxPlotChooser.removeAllItems();

                for (BoxPlotInfo boxPlotInfo : BoxPlotGenerator.getInstance().getBoxPlotInfoList()) {
                    boxPlotChooser.addItem(boxPlotInfo.name);
                }

                deleteDialog.setVisible(true);
            }
        });

        exportItem.addActionListener(new ActionListener() {

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(getParent());

                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    if(!endsWithPNG(selectedFile.getName())) {
                        selectedFile = new File(selectedFile.getPath() + ".png");
                    }

                    try {
                        if(!selectedFile.exists()) {
                            selectedFile.createNewFile();
                        }

                        OutputStream outputStream = new FileOutputStream(selectedFile);

                        BoxPlotGenerator.getInstance().exportCurrent(outputStream);

                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        scalingItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                scalingSpinner.setModel(new SpinnerNumberModel(BoxPlotGenerator.getInstance().getGlobalScaling(),
                        1, null, 1));

                scalingDialog.setVisible(true);
            }
        });

        manageMenu.add(removeItem);
        manageMenu.add(exportItem);
        manageMenu.addSeparator();
        manageMenu.add(scalingItem);

        this.add(manageMenu);
    }

    private boolean endsWithPNG(String str) {
        return str.length() > 4 && str.substring(str.length() - 4).equalsIgnoreCase(".png");
    }
}
