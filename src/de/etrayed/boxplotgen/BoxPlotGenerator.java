package de.etrayed.boxplotgen;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.FileExporter;
import de.etrayed.boxplotgen.visual.Window;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Etrayed
 */
public class BoxPlotGenerator {

    private static BoxPlotGenerator instance;

    private final FileExporter fileExporter;

    private final List<BoxPlotInfo> boxPlotInfoList = new ArrayList<>();

    private final Window window;

    private int globalScaling;

    private BoxPlotGenerator() {
        this.fileExporter = new FileExporter();
        this.window = new Window();
    }

    void openWindow() {
        window.open();
    }

    public void repaintCanvas() {
        window.repaintCanvas();
    }

    public void exportCurrentToFile(OutputStream outputStream) {
        try {
            fileExporter.export(outputStream, globalScaling);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<BoxPlotInfo> getBoxPlotInfoList() {
        return boxPlotInfoList;
    }

    public void setWindowScrollBarMaximum(int maximum) {
        window.setScrollBarMaximum(maximum);
    }

    public int getGlobalScaling() {
        return globalScaling;
    }

    public void setGlobalScaling(int globalScaling) {
        this.globalScaling = globalScaling;
    }

    public static BoxPlotGenerator getInstance() {
        return instance;
    }

    static BoxPlotGenerator newInstance() {
        return instance = new BoxPlotGenerator();
    }
}
