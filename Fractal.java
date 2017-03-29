import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Abstract class for Fractals. Contains info about zoom, pan,
 * and fractal settings.
 */
public abstract class Fractal {
    private final FractalType mType;
    private FractalSetting[] mFractalSettings;
    private FractalListener mListener;
    private double mZoom = 0.75;
    private int mPanX = 0;
    private int mPanY = 0;

    Fractal(FractalType type) {
        mType = type;
    }

    void zoom(double amount) {
        if (mZoom + amount / 10 > 0) {
            amount /= 10;
            mZoom += amount;
            invalidate();
        }
    }

    void pan(int panX, int panY) {
        mPanX = panX;
        mPanY = panY;
        invalidate();
    }

    double getZoom() {
        return mZoom;
    }

    int getPanX() {
        return mPanX;
    }

    int getPanY() {
        return mPanY;
    }

    FractalSetting[] getFractalSettings() {
        if (mFractalSettings == null) {
            mFractalSettings = buildFractalSettings();
        }
        return mFractalSettings;
    }

    FractalType getFractalType() {
        return mType;
    }

    // A listener is bound to the Fractal so it knows when to redraw the Fractal
    void setFractalListener(FractalListener listener) {
        mListener = listener;
    }

    // Helper function for letting a listener know the Fractal needs to be redrawn
    void invalidate() {
        if (mListener != null) {
            mListener.onFractalInvalidated();
        }
    }

    abstract void draw(Graphics g, int width, int height);

    abstract FractalSetting[] buildFractalSettings();

    interface FractalListener {
        void onFractalInvalidated();
    }

    // Constants containing the names of the FractalTypes
    private static final String FRACTAL_TYPE_KOCH = "Koch snowflake";
    private static final String FRACTAL_TYPE_SIERPINSKI = "Sierpinski carpet";
    private static final String FRACTAL_TYPE_MANDELBROT = "Mandelbrot set";
    private static final String FRACTAL_TYPE_JULIA = "Julia set";

    // The below functions are used for serialization

    // Get a Fractal object by FractalType.
    static Fractal getFractal(FractalType type) {
        switch (type) {
            case Koch: return new FractalKoch();
            case Sierpinski: return new FractalSierpinski();
            case Mandelbrot: return new FractalMandelbrot();
            case Julia: return new FractalJulia();
            default: return null;
        }
    }

    // Get a FractalType's name by FractalType
    static String getFractalTypeName(FractalType type) {
        switch (type) {
            case Koch: return FRACTAL_TYPE_KOCH;
            case Sierpinski: return FRACTAL_TYPE_SIERPINSKI;
            case Mandelbrot: return FRACTAL_TYPE_MANDELBROT;
            case Julia: return FRACTAL_TYPE_JULIA;
            default: return null;
        }
    }

    // Get a FractalType's mnemonic key code by FractalType
    static int getFractalTypeMnemonic(FractalType type) {
        switch (type) {
            case Koch: return KeyEvent.VK_K;
            case Sierpinski: return KeyEvent.VK_S;
            case Mandelbrot: return KeyEvent.VK_M;
            case Julia: return KeyEvent.VK_J;
            default: return -1;
        }
    }

    // Get a FractalType by its name
    static FractalType getFractalType(String name) {
        if (name.equals(FRACTAL_TYPE_KOCH)) {
            return FractalType.Koch;
        } else if (name.equals(FRACTAL_TYPE_SIERPINSKI)) {
            return FractalType.Sierpinski;
        } else if (name.equals(FRACTAL_TYPE_MANDELBROT)) {
            return FractalType.Mandelbrot;
        } else if (name.equals(FRACTAL_TYPE_JULIA)) {
            return FractalType.Julia;
        }

        return null;
    }

    enum FractalType {
        Koch, Sierpinski, Mandelbrot, Julia
    }
}
