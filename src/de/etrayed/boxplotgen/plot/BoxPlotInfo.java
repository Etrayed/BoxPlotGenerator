package de.etrayed.boxplotgen.plot;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoxPlotInfo that = (BoxPlotInfo) o;

        return Double.compare(that.minimum, minimum) == 0 &&
                Double.compare(that.lowerQuartile, lowerQuartile) == 0 &&
                Double.compare(that.median, median) == 0 &&
                Double.compare(that.upperQuartile, upperQuartile) == 0 &&
                Double.compare(that.maximum, maximum) == 0 &&
                Float.compare(that.scaling, scaling) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimum, lowerQuartile, median, upperQuartile, maximum, scaling);
    }

    @Override
    public String toString() {
        return "BoxPlotInfo{" +
                "minimum=" + minimum +
                ", lowerQuartile=" + lowerQuartile +
                ", median=" + median +
                ", upperQuartile=" + upperQuartile +
                ", maximum=" + maximum +
                ", scaling=" + scaling +
                '}';
    }

    public static BoxPlotInfo createNew(double[] values, float scaling) {
        return BoxPlotValueCalculator.withValues(values, scaling);
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
