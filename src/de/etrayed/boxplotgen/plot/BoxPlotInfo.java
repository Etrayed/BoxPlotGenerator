package de.etrayed.boxplotgen.plot;

import java.util.Objects;

/**
 * @author Etrayed
 */
public class BoxPlotInfo {

    public final String name;

    public final double minimum, maximum;

    final double lowerQuartile, median, upperQuartile;

    private BoxPlotInfo(String name, double minimum, double lowerQuartile, double median, double upperQuartile,
                        double maximum) {
        this.name = name;
        this.minimum = minimum;
        this.lowerQuartile = lowerQuartile;
        this.median = median;
        this.upperQuartile = upperQuartile;
        this.maximum = maximum;
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
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, minimum, lowerQuartile, median, upperQuartile, maximum);
    }

    @Override
    public String toString() {
        return "BoxPlotInfo{" +
                "name='" + name + '\'' +
                ", minimum=" + minimum +
                ", lowerQuartile=" + lowerQuartile +
                ", median=" + median +
                ", upperQuartile=" + upperQuartile +
                ", maximum=" + maximum +
                '}';
    }

    public static BoxPlotInfo createNew(String name, double[] values) {
        return BoxPlotValueCalculator.withValues(name, values);
    }

    public static BoxPlotInfo createNew(String name, double minimum, double lowerQuartile, double median,
                                        double upperQuartile, double maximum) {
        ensureRatio(minimum, lowerQuartile);
        ensureRatio(lowerQuartile, median);
        ensureRatio(median, upperQuartile);
        ensureRatio(upperQuartile, maximum);

        return new BoxPlotInfo(name, minimum, lowerQuartile, median, upperQuartile, maximum);
    }

    private static void ensureRatio(double d1, double d2) {
        if(d1 > d2) {
            throw new IllegalArgumentException("Incorrect ratio: " + d1 + " > " + d2);
        }
    }
}
