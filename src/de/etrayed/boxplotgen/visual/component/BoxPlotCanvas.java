package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.BoxPlotGenerator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Etrayed
 */
public class BoxPlotCanvas extends JComponent {

    private int scaling = 1;

    private int startY;

    public BoxPlotCanvas() {
        setBounds(0, 0, 300, 700);
        setVisible(true);
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getScaling() {
        return scaling;
    }

    public void setScaling(int scaling) {
        this.scaling = scaling;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if(BoxPlotGenerator.getInstance().getBoxPlotInfoList().size() == 0) {
            return;
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            BoxPlotGenerator.getInstance().exportCurrent(outputStream);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

            BufferedImage image = ImageIO.read(inputStream);

            inputStream.close();

            ((Graphics2D) graphics).drawImage(image.getSubimage(0, startY, 300, 700 - startY), null, 0, 0);

            BoxPlotGenerator.getInstance().setWindowScrollBarMaximum(image.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
