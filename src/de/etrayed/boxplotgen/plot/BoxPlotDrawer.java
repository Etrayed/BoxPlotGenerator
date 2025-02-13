package de.etrayed.boxplotgen.plot;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * @author Etrayed
 */
public class BoxPlotDrawer {

    private static final List<Color> ALL_COLORS = new CopyOnWriteArrayList<>();

    private static final Font FONT = new Font("Calibri", Font.PLAIN, 17);

    private static final Random COLOR_RANDOM = new Random();

    public static final int SEPARATING = 65;

    private static final int SCALA_BASE_X = 55;

    private static final int PLOT_MIDDLE_X = SCALA_BASE_X + ((300 - SCALA_BASE_X) / 2);

    static {
        ALL_COLORS.addAll(Arrays.asList(Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green,
                Color.lightGray, Color.magenta, Color.pink, Color.red, Color.orange, Color.YELLOW));
    }

    static Color randomColor(Predicate<Color> colorPredicate) {
        Color color;

        do {
            color = ALL_COLORS.get(COLOR_RANDOM.nextInt(12));
        } while (colorPredicate.test(color));

        return color;
    }

    public static void drawOn(Graphics2D graphics, BoxPlotInfo[] info, double minimum, double maximum, double scaling, int height) {
        drawScala(graphics, minimum, maximum, scaling, height);

        if(info.length == 1) {
            drawPlot(graphics, info[0], Color.black, minimum, scaling, height);
        } else {
            for (BoxPlotInfo boxPlotInfo : info) {
                drawPlot(graphics, boxPlotInfo, boxPlotInfo.color, minimum, scaling, height);
            }

            drawLegend(graphics, height, info);
        }
    }

    private static void drawScala(Graphics2D graphics, double minimum, double maximum, double scaling, int height) {
        graphics.setColor(Color.gray);
        graphics.setFont(FONT);

        graphics.drawLine(SCALA_BASE_X, 5, SCALA_BASE_X, height - 5);

        int max = height - 5;
        double count = minimum;

        for (int y = height - SEPARATING - 5; y >= minimum; y -= SEPARATING, count += scaling) {
            graphics.drawLine(SCALA_BASE_X - 5, y, SCALA_BASE_X, y);

            String number = limit(String.valueOf(count == (int) count ? (int) count : count), 4);

            graphics.drawString(number, SCALA_BASE_X - 12 - stringWidth(graphics, number),
                    y + 6);
        }
    }

    private static String limit(String str, int max) {
        if(str.length() > max) {
            return str.substring(0, max + 1);
        }

        return str;
    }

    private static int stringWidth(Graphics2D graphics, String str) {
        int result = graphics.getFontMetrics().stringWidth(str);

        if(result > (SCALA_BASE_X - 15)) {
            return 0;
        }

        return result;
    }

    private static void drawPlot(Graphics2D graphics, BoxPlotInfo info, Color color, double minimum, double scaling, int height) {
        graphics.setColor(color);

        double onePixel = (double) SEPARATING / scaling;
        int y = (int) ((height - SEPARATING - 5) - (onePixel * (info.minimum - minimum)));
        int y1;

        graphics.drawLine(PLOT_MIDDLE_X - 40, y, PLOT_MIDDLE_X + 40, y);

        y1 = (int) (y - (onePixel * (info.lowerQuartile - info.minimum)));

        graphics.drawLine(PLOT_MIDDLE_X, y, PLOT_MIDDLE_X, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.median - info.lowerQuartile)));

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X - 90, y1);
        graphics.drawLine(PLOT_MIDDLE_X + 90, y, PLOT_MIDDLE_X + 90, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.upperQuartile - info.median)));

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X - 90, y1);
        graphics.drawLine(PLOT_MIDDLE_X + 90, y, PLOT_MIDDLE_X + 90, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.maximum - info.upperQuartile)));

        graphics.drawLine(PLOT_MIDDLE_X, y, PLOT_MIDDLE_X, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 40, y, PLOT_MIDDLE_X + 40, y);
    }

    private static void drawLegend(Graphics2D graphics, int height, BoxPlotInfo[] info) {
        AtomicInteger y = new AtomicInteger(height);

        for (BoxPlotInfo boxPlotInfo : info) {
            graphics.setColor(boxPlotInfo.color);

            graphics.fillRect(SCALA_BASE_X + 5, y.get() + 4, 12 ,12);

            graphics.setFont(graphics.getFont().deriveFont(14F));
            graphics.setColor(Color.gray);

            graphics.drawString(boxPlotInfo.name, SCALA_BASE_X + 20, y.getAndAdd(20) + 14);
        }
    }
}
