package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.Callback;
import de.etrayed.boxplotgen.util.EasyCallable;
import de.etrayed.boxplotgen.visual.Window;

import javax.swing.*;
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
                        callback.handleSuccess(infoCallable.call());
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
        return new EasyCallable<>(null);
    }

    private Callable<BoxPlotInfo> setupCalculated() {
        return new EasyCallable<>(null);
    }
}
