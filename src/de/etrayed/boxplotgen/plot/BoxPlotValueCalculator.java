package de.etrayed.boxplotgen.plot;

import java.util.Arrays;

/**
 * @author Etrayed
 */
class BoxPlotValueCalculator {

    static BoxPlotInfo withValues(double[] values) {
        double minimum;
        double lowerQuartile;
        double median;
        double upperQuartile;
        double maximum;

        float scaling;

        Arrays.sort(values);

        minimum = values[0];
        maximum = values[values.length - 1];

        lowerQuartile = values[0]; // half of the half
        upperQuartile = values[0]; // three quarters

        median = values[values.length / 2]; // only if odd number

        scaling = 0; // ??? separate method idk

        return BoxPlotInfo.createNew(minimum, lowerQuartile, median, upperQuartile, maximum, scaling);
    }
}
