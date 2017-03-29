import javax.swing.*;
import java.awt.*;

/**
 * Implementation of the Koch snowflake fractal.
 */
public class FractalKoch extends Fractal {
    FractalKoch() {
        super(FractalType.Koch);
    }

    @Override
    void draw(Graphics g, int width, int height) {
        // Draw background
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, width, height);

        // Set color to line color
        g.setColor(getLineColor());

        // Take the smallest of width and height and use it as dimension for the triangle
        int smallestDimension = Math.min(width, height);

        // Apply
        double triangleWH = smallestDimension * getZoom();

        // Calculate left and top distances and apply pan
        double left = (width - triangleWH) / 2d + getPanX();
        double top = (height - triangleWH) / 64d + getPanY();

        // Start drawing!
        drawKochStart(g, left, top + triangleWH, left + triangleWH, top + triangleWH);
    }

    void drawKochStart(Graphics g, double x1, double y1, double x2, double y2) {
        // Calculate triangle line segments that belong to this base
        double[] lineSegments = getLineSegmentsOnBase(x1, y1, x2, y2);

        // Draw base
        drawLine(g, x1, y1, x2, y2);

        // Draw other line segments
        drawLine(g, lineSegments[0], lineSegments[1], lineSegments[2], lineSegments[3]);
        drawLine(g, lineSegments[4], lineSegments[5], lineSegments[6], lineSegments[7]);

        int finalIteration = getIterations();

        // Final iteration not infinite nor 1?
        if (finalIteration != 1) {
            // Start recursive koch drawing on all surfaces
            drawKochRecursive(g, 1, finalIteration, x2, y2, x1, y1);
            drawKochRecursive(g, 1, finalIteration, lineSegments[0], lineSegments[1], lineSegments[2], lineSegments[3]);
            drawKochRecursive(g, 1, finalIteration, lineSegments[4], lineSegments[5], lineSegments[6], lineSegments[7]);
        }
    }

    void drawKochRecursive(Graphics g, int iteration, int finalIteration, double x1, double y1, double x2, double y2) {
        // Increment iteration counter
        iteration ++;

        // Store the deltas / 3
        double xd = (x2 - x1) / 3;
        double yd = (y2 - y1) / 3;

        // Current iteration koch base
        double nx1 = x1 + xd;
        double ny1 = y1 + yd;
        double nx2 = x2 - xd;
        double ny2 = y2 - yd;

        // Get associated line segments
        double[] lineSegments = getLineSegmentsOnBase(nx1, ny1, nx2, ny2);

        // Draw the line segments
        drawLine(g, lineSegments[0], lineSegments[1], lineSegments[2], lineSegments[3]);
        drawLine(g, lineSegments[4], lineSegments[5], lineSegments[6], lineSegments[7]);

        // Are there iterations left?
        if (iteration != finalIteration) {
            // Make sure the result is still visible in case of infinite iterations
            if (finalIteration == 0 && Math.max(Math.abs(nx2 - nx1), Math.abs(ny2 - ny1)) < 1) {
                return;
            }

            // Draw on the 4 surfaces that are now there
            drawKochRecursive(g, iteration, finalIteration,
                    lineSegments[0], lineSegments[1], lineSegments[2], lineSegments[3]);

            drawKochRecursive(g, iteration, finalIteration,
                    lineSegments[4], lineSegments[5], lineSegments[6], lineSegments[7]);

            drawKochRecursive(g, iteration, finalIteration, x1, y1, x1 + xd, y1 + yd);

            drawKochRecursive(g, iteration, finalIteration, x1 + xd * 2, y1 + yd * 2, x2, y2);
        }

    }

    // Gets the two line segments needed to create a triangle from a base line segment.
    double[] getLineSegmentsOnBase(double x1, double y1, double x2, double y2) {
        double angle = Math.PI / 3; // 60 deg
        double x3 = x1 + (x2 - x1) * Math.cos(angle) + (y2 - y1) * Math.sin(angle);
        double y3 = y1 - (x2 - x1) * Math.sin(angle) + (y2 - y1) * Math.cos(angle);

        return new double[] { x1, y1, x3, y3, x3, y3, x2, y2 };
    }

    // Helper function for drawing lines, to achieve max precision up until the drawing phase.
    static void drawLine(Graphics g, double x1, double y1, double x2, double y2) {
        // Convert all doubles to ints
        int ix1 = (int) Math.round(x1);
        int iy1 = (int) Math.round(y1);
        int ix2 = (int) Math.round(x2);
        int iy2 = (int) Math.round(y2);
        g.drawLine(ix1, iy1, ix2, iy2);
    }

    // The settings for this fractal
    @Override
    FractalSetting[] buildFractalSettings() {
        return new FractalSetting[] {
                new FractalSettingColor(this, Color.WHITE, "Background color"),
                new FractalSettingColor(this, Color.BLACK, "Line color"),
                new FractalSettingNumber(this, new SpinnerNumberModel(0, 0, 1000, 1), "Iterations")
        };
    }

    // Helper functions for getting values from FractalSettings
    private Color getBackgroundColor() {
        return FractalSetting.getColorAt(getFractalSettings(), 0);
    }

    private Color getLineColor() {
        return FractalSetting.getColorAt(getFractalSettings(), 1);
    }

    private int getIterations() {
        return FractalSetting.getNumberAt(getFractalSettings(), 2);
    }
}
