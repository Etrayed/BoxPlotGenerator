package de.etrayed.boxplotgen.visual;

import de.etrayed.boxplotgen.visual.component.BPGMenuBar;
import de.etrayed.boxplotgen.visual.component.BoxPlotCanvas;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Etrayed
 */
public class Window {

    public static final BufferedImage ICON;

    private static final int DEFAULT_SCROLL_BAR_MAX = 700; // TODO: Fix ScrollBar

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            //noinspection ConstantConditions
            ICON = ImageIO.read(ClassLoader.getSystemResourceAsStream("icon.png"));
        } catch (IOException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException
                | ClassNotFoundException e) {
            throw new InternalError(e);
        }
    }

    private final JFrame frame;

    private final Adjustable scrollBar;

    private final BoxPlotCanvas canvas;

    public Window() {
        this.frame = new JFrame("BoxPlotGenerator");
        this.scrollBar = new JScrollBar(JScrollBar.VERTICAL, 0, DEFAULT_SCROLL_BAR_MAX, 0, DEFAULT_SCROLL_BAR_MAX);
        this.canvas = new BoxPlotCanvas();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setIconImage(ICON);
        frame.setBounds(center(screenSize.width, 335), center(screenSize.height, 700), 335, 700);
        frame.setJMenuBar(new BPGMenuBar(frame));

        ((JScrollBar) scrollBar).setBounds(300, 0, 30, 630);
        scrollBar.addAdjustmentListener(new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                canvas.setStartY(e.getValue());
                canvas.repaint();
            }
        });

        frame.add((Component) scrollBar);
        frame.add(canvas);
    }

    public void open() {
        frame.setVisible(true);
    }

    public void setScrollBarMaximum(int maximum) {
        if(maximum < DEFAULT_SCROLL_BAR_MAX) {
            return;
        }

        this.scrollBar.setMaximum(maximum);
    }

    public void repaintCanvas() {
        canvas.repaint();
    }

    public static int center(int full, int value) {
        return (full - value) / 2;
    }
}
