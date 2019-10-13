package de.etrayed.boxplotgen.plot;

import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author Etrayed
 */
public class BoxPlotInfo {

    private static final Map<BoxPlotInfo, Color> COLOR_MAP = new ConcurrentHashMap<>();

    public final String name;

    public final double minimum, maximum;

    final double lowerQuartile, median, upperQuartile;

    final Color color;

    @SuppressWarnings("Anonymous2MethodRef")
    private BoxPlotInfo(String name, double minimum, double lowerQuartile, double median, double upperQuartile,
                        double maximum) {
        this.name = name;
        this.minimum = minimum;
        this.lowerQuartile = lowerQuartile;
        this.median = median;
        this.upperQuartile = upperQuartile;
        this.maximum = maximum;
        this.color = BoxPlotDrawer.randomColor(new Predicate<Color>() {

            @Override
            public boolean test(Color color) {
                return COLOR_MAP.containsValue(color);
            }
        });
    }

    @Override
    protected void finalize() {
        COLOR_MAP.remove(this);
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
            throw new IllegalArgumentException(d1 + " > " + d2);
        }
    }
}
