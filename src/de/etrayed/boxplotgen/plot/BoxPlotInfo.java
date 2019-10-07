package de.etrayed.boxplotgen.plot;

/**
 * @author Etrayed
 */
public class BoxPlotInfo {

    private final double minimum, lowerQuartile, median, upperQuartile, maximum;

    private final float scaling;

    private BoxPlotInfo(double minimum, double lowerQuartile, double median, double upperQuartile, double maximum,
                        float scaling) {
        this.minimum = minimum;
        this.lowerQuartile = lowerQuartile;
        this.median = median;
        this.upperQuartile = upperQuartile;
        this.maximum = maximum;
        this.scaling = scaling;
    }

    public double getMinimum() {
        return minimum;
    }

    public double getLowerQuartile() {
        return lowerQuartile;
    }

    public double getMedian() {
        return median;
    }

    public double getUpperQuartile() {
        return upperQuartile;
    }

    public double getMaximum() {
        return maximum;
    }

    public float getScaling() {
        return scaling;
    }

    public static BoxPlotInfo createNew(double[] values) {
        return BoxPlotValueCalculator.withValues(values);
    }

    public static BoxPlotInfo createNew(double minimum, double lowerQuartile, double median, double upperQuartile,
                                        double maximum, float scaling) {
        ensureRatio(minimum, lowerQuartile);
        ensureRatio(lowerQuartile, median);
        ensureRatio(median, upperQuartile);
        ensureRatio(upperQuartile, maximum);

        return new BoxPlotInfo(minimum, lowerQuartile, median, upperQuartile, maximum, scaling);
    }

    private static void ensureRatio(double d1, double d2) {
        if(d1 > d2) {
            throw new IllegalArgumentException("Incorrect ratio: " + d1 + " > " + d2);
        }
    }
}
