package de.etrayed.boxplotgen.util;

import de.etrayed.boxplotgen.BoxPlotGenerator;
import de.etrayed.boxplotgen.plot.BoxPlotDrawer;
import de.etrayed.boxplotgen.plot.BoxPlotInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * @author Etrayed
 */
public class FileExporter {

    public void export(Path path, int scaling) throws IOException {
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

        try(OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            ImageIO.write(image, "PNG", outputStream);
        }
    }
}
