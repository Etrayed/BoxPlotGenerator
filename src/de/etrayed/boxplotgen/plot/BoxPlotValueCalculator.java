package de.etrayed.boxplotgen.plot;

import java.util.Arrays;

/**
 * @author Etrayed
 */
class BoxPlotValueCalculator {

    static BoxPlotInfo withValues(double[] values, int scaling) {
        double minimum;
        double lowerQuartile;
        double median;
        double upperQuartile;
        double maximum;

        Arrays.sort(values);

        minimum = values[0];
        maximum = values[values.length - 1];

        int halfLength = values.length / 2;
        boolean evenLength = values.length % 2 == 0;

        lowerQuartile = lowerQuartile(values, halfLength, evenLength);
        upperQuartile = upperQuartile(values, halfLength, evenLength);

        median = median(values);

        return BoxPlotInfo.createNew(minimum, lowerQuartile, median, upperQuartile, maximum, scaling);
    }

    private static double lowerQuartile(double[] values, int halfLength, boolean evenLength) {
        if(evenLength) {
            return values[(halfLength - 1) / 2];
        }

        int oneQuarter = halfLength / 2;

        return (values[oneQuarter] + values[oneQuarter - 1]) / 2;
    }

    private static double upperQuartile(double[] values, int halfLength, boolean evenLength) {
        if(evenLength) {
            return values[halfLength + (halfLength / 2)];
        }

        int threeQuarters = halfLength + (halfLength / 2);

        return (values[threeQuarters] + values[threeQuarters + 1]) / 2;
    }

    private static double median(double[] values) {
        int halfLength = values.length / 2;

        if(values.length % 2 == 0) {
            return (values[halfLength] + values[halfLength - 1]) / 2;
        } else {
            return values[halfLength];
        }
    }
}
