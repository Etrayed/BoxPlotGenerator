package de.etrayed.boxplotgen.plot;

import java.awt.*;

/**
 * @author Etrayed
 */
public class BoxPlotDrawer {

    public static final int SEPARATING = 65;

    private static final int SCALA_BASE_X = 45;

    private static final int PLOT_MIDDLE_X = SCALA_BASE_X + ((300 - SCALA_BASE_X) / 2);

    static void drawOn(Graphics2D graphics, BoxPlotInfo info, int height) {
        drawScala(graphics, info, height);

        drawPlot(graphics, info, height);
    }

    private static void drawScala(Graphics2D graphics, BoxPlotInfo info, int height) {
        graphics.setColor(Color.gray);
        graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, 15F));

        graphics.drawLine(SCALA_BASE_X, 5, SCALA_BASE_X, height - 5);

        int max = height - 5;

        for (int y = SEPARATING + 5, count = ((max - y) / SEPARATING) - 1; y < max; y+= SEPARATING, count--) {
            graphics.drawLine(SCALA_BASE_X - 5, y, SCALA_BASE_X, y);

            String number = String.valueOf((int) info.getMinimum() - 1 + (count * info.getScaling()));

            graphics.drawString(number, SCALA_BASE_X - 12 - stringWidth(graphics, number),
                    y + 6);
        }
    }

    private static int stringWidth(Graphics2D graphics, String str) {
        int result = graphics.getFontMetrics().stringWidth(str);

        if(result > (SCALA_BASE_X - 15)) {
            return 0;
        }

        return result;
    }

    private static void drawPlot(Graphics2D graphics, BoxPlotInfo info, int height) {
        graphics.setColor(Color.black);

        double onePixel = (double) SEPARATING / info.getScaling();
        int y = height - SEPARATING - 5;
        int y1;

        graphics.drawLine(PLOT_MIDDLE_X - 40, y, PLOT_MIDDLE_X + 40, y);

        y1 = (int) (y - (onePixel * (info.getLowerQuartile() - info.getMinimum())));

        graphics.drawLine(PLOT_MIDDLE_X, y, PLOT_MIDDLE_X, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.getMedian() - info.getLowerQuartile())));

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X - 90, y1);
        graphics.drawLine(PLOT_MIDDLE_X + 90, y, PLOT_MIDDLE_X + 90, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.getUpperQuartile() - info.getMedian())));

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X - 90, y1);
        graphics.drawLine(PLOT_MIDDLE_X + 90, y, PLOT_MIDDLE_X + 90, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 90, y, PLOT_MIDDLE_X + 90, y);

        y1 = (int) (y - (onePixel * (info.getMaximum() - info.getUpperQuartile())));

        graphics.drawLine(PLOT_MIDDLE_X, y, PLOT_MIDDLE_X, y1);

        y = y1;

        graphics.drawLine(PLOT_MIDDLE_X - 40, y, PLOT_MIDDLE_X + 40, y);
    }
}
