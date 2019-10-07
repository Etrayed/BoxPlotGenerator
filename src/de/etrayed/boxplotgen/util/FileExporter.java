package de.etrayed.boxplotgen.util;

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

/**
 * @author Etrayed
 */
public class FileExporter {

    public void export(Path path, BoxPlotInfo info) throws IOException {
        int height = BoxPlotDrawer.SEPARATING * (((int) (info.getMaximum() - info.getMinimum()) / info.getScaling()) + 2);

        height += 10;

        BufferedImage image = new BufferedImage(300, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicsCopy = (Graphics2D) image.getGraphics().create();

        graphicsCopy.setColor(Color.white);
        graphicsCopy.fillRect(0, 0, 300, height);

        info.drawOn(graphicsCopy, height);

        graphicsCopy.dispose();

        try(OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            ImageIO.write(image, "PNG", outputStream);
        }
    }
}
