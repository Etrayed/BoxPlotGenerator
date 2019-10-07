package de.etrayed.boxplotgen;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.FileExporter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Etrayed
 */
public class BoxPlotGenerator {

    private static BoxPlotGenerator instance;

    private final FileExporter fileExporter;

    private BoxPlotGenerator() {
        this.fileExporter = new FileExporter();
    }

    public void exportToFile(Path outputPath, BoxPlotInfo info) throws IOException {
        fileExporter.export(outputPath, info);
    }

    public static BoxPlotGenerator getInstance() {
        return instance;
    }

    static BoxPlotGenerator newInstance() {
        return instance = new BoxPlotGenerator();
    }
}
