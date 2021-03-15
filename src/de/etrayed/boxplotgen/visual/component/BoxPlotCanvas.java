package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.BoxPlotGenerator;
import de.etrayed.boxplotgen.visual.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Etrayed
 */
public class BoxPlotCanvas extends JComponent {

    private int startY;

    private BufferedImage cached;

    public BoxPlotCanvas() {
        setBounds(0, 0, 300, 700);
        setVisible(true);
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public void resetCache() {
        cached = null;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if(BoxPlotGenerator.getInstance().getBoxPlotInfoList().size() == 0) {
            BoxPlotGenerator.getInstance().setWindowScrollBarMaximum(Window.DEFAULT_SCROLL_BAR_MAX);
            return;
        }

        try {
            if(cached == null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                BoxPlotGenerator.getInstance().exportCurrent(outputStream);

                try (ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray())) {
                    cached = ImageIO.read(inputStream);
                }
            }

            try {
                ((Graphics2D) graphics).drawImage(cached.getSubimage(0, startY, 300, cached.getHeight() - startY),
                        null, 0, 0);
            } catch (RasterFormatException e) {
                BoxPlotGenerator.getInstance().resetScrolling(cached.getHeight());

                return;
            }

            BoxPlotGenerator.getInstance().setWindowScrollBarMaximum(cached.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
