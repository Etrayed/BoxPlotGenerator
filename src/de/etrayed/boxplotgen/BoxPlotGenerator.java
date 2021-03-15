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

    private double globalScaling = 1;

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

    public void exportCurrent(OutputStream outputStream) {
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

    public double getGlobalScaling() {
        return globalScaling;
    }

    public void setGlobalScaling(double globalScaling) {
        this.globalScaling = globalScaling;
    }

    public void resetScrolling(int max) {
        window.resetScrolling(max);
    }

    public void resetCanvasCache() {
        window.resetCanvasCache();
    }

    public static BoxPlotGenerator getInstance() {
        return instance;
    }

    static BoxPlotGenerator newInstance() {
        return instance = new BoxPlotGenerator();
    }
}
