import javax.swing.*;
import java.awt.*;

/**
 * Impl. of Sierpinski fractal
 */
public class FractalSierpinski extends Fractal {
    FractalSierpinski() {
        super(FractalType.Sierpinski);
    }

    @Override
    void draw(Graphics g, int width, int height) {
        // Draw background
        g.setColor(getBackgroundColor());
        g.fillRect(0, 0, width, height);

        // Set foreground color
        g.setColor(getForegroundColor());

        // Get iterations
        int iterations = getIterations();

        // Take the smallest of width and height and use it as dimension for the first square
        double dimension = Math.min(width, height) / 4 * getZoom();

        // Calculate left and top distances and apply pan
        double left = (width - dimension) / 2 + getPanX();
        double top = (height - dimension) / 2 + getPanY();

        g.fillRect((int) left, (int) top, (int) dimension, (int) dimension);

        // More than 1 iterations?
        if (iterations != 1) {
            // Start drawing recursively
            drawSierpinskiRecursive(g, 1, iterations, left, top, dimension);
        }
    }

    private void drawSierpinskiRecursive(Graphics g, int iteration, int finalIteration, double left, double top, double dimension) {
        iteration ++;

        dimension /= Math.pow(3, iteration - 1);

        double[] squares = new double[] {
                left - dimension * 2, top - dimension * 2,
                left + dimension, top - dimension * 2,
                left + dimension * 4, top - dimension * 2,

                left - dimension * 2, top + dimension,
                left + dimension * 4, top + dimension,

                left - dimension * 2, top + dimension * 4,
                left + dimension, top + dimension * 4,
                left + dimension * 4, top + dimension * 4
        };

        for (int i = 0; i < squares.length; i += 2) {
            fillRect(g, squares[i], squares[i + 1], dimension, dimension);
        }

        if (iteration != finalIteration) {
            if (finalIteration == 0) {
                return;
            }
            for (int i = 0; i < squares.length; i += 2) {
                drawSierpinskiRecursive(g, iteration, finalIteration, squares[i], squares[i + 1], dimension);
            }
        }
    }

    private static void fillRect(Graphics g, double x, double y, double width, double height) {
        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);
        int iwidth = (int) Math.round(width);
        int iheight = (int) Math.round(height);
        g.fillRect(ix, iy, iwidth, iheight);
    }

    // The settings for this fractal
    @Override
    FractalSetting[] buildFractalSettings() {
        return new FractalSetting[] {
                new FractalSettingColor(this, new Color(253, 226, 134), "Background color"),
                new FractalSettingColor(this, new Color(73, 127, 243), "Foreground color"),
                new FractalSettingNumber(this, new SpinnerNumberModel(0, 0, 1000, 1), "Iterations")
        };
    }

    // Helper functions for getting values from FractalSettings
    private Color getBackgroundColor() {
        return FractalSetting.getColorAt(getFractalSettings(), 0);
    }

    private Color getForegroundColor() {
        return FractalSetting.getColorAt(getFractalSettings(), 1);
    }

    private int getIterations() {
        return FractalSetting.getNumberAt(getFractalSettings(), 2);
    }
}
