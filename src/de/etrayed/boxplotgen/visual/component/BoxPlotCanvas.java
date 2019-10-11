package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.BoxPlotGenerator;
import de.etrayed.boxplotgen.plot.BoxPlotDrawer;
import de.etrayed.boxplotgen.plot.BoxPlotInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

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
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        List<BoxPlotInfo> boxPlotInfoList = BoxPlotGenerator.getInstance().getBoxPlotInfoList();

        if(boxPlotInfoList.size() == 0) {
            return;
        }

        BoxPlotInfo[] boxPlotInfoArray = boxPlotInfoList.toArray(new BoxPlotInfo[boxPlotInfoList.size()]);
        double highestMaximum = boxPlotInfoArray[0].maximum;
        double lowestMinimum = boxPlotInfoArray[0].minimum;

        if(boxPlotInfoArray.length > 1) {
            int index = 1;

            while (index < boxPlotInfoArray.length) {
                if(boxPlotInfoArray[index].maximum > highestMaximum) {
                    highestMaximum = boxPlotInfoArray[index].maximum;
                }

                if(boxPlotInfoArray[index].minimum < lowestMinimum) {
                    lowestMinimum = boxPlotInfoArray[index].minimum;
                }

                index++;
            }
        }

        int height = (BoxPlotDrawer.SEPARATING * (((int) (highestMaximum - lowestMinimum) / scaling) + 2)) + 10;

        BufferedImage image = new BufferedImage(300, height + (boxPlotInfoList.size() == 1 ? 0
                : (boxPlotInfoList.size() * 20)), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicsCopy = (Graphics2D) image.getGraphics().create();

        graphicsCopy.setColor(Color.white);
        graphicsCopy.fillRect(0, 0, 300, image.getHeight());

        BoxPlotDrawer.drawOn(graphicsCopy, boxPlotInfoArray, lowestMinimum, highestMaximum, scaling, height);

        graphicsCopy.dispose();

        graphics.drawImage(image.getSubimage(0, startY, 300, 700 - startY), null, 0, 0);

        BoxPlotGenerator.getInstance().setWindowScrollBarMaximum(image.getHeight());
    }
}
