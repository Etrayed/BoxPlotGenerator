package de.etrayed.boxplotgen.util;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Etrayed
 */
public class FileExporter {

    public void export(Path path, BoxPlotInfo info) throws IOException {
        BufferedImage image = new BufferedImage(300, 500 /* maybe custom? */, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphicsCopy = (Graphics2D) image.getGraphics().create();

        graphicsCopy.setColor(Color.white);
        graphicsCopy.fillRect(0, 0, 300, 500 /* customize */);

        info.drawOn(graphicsCopy);

        graphicsCopy.dispose();

        try(OutputStream outputStream = Files.newOutputStream(path)) {
            ImageIO.write(image, "PNG", outputStream);
        }
    }
}
