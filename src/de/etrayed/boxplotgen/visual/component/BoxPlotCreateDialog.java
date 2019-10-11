package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.Callback;
import de.etrayed.boxplotgen.util.EasyCallable;

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
