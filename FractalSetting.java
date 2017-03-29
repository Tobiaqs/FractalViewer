import javax.swing.*;
import java.awt.*;

/**
 * Base class for FractalSettings. This class makes it easy to assign
 * settings to Fractals.
 */
public abstract class FractalSetting {
    protected final Fractal mFractal;
    private JComponent mJComponent;

    FractalSetting(Fractal fractal) {
        mFractal = fractal;
    }

    // Function used for preventing duplicate JComponents
    JComponent getJComponent() {
        if (mJComponent == null) {
            mJComponent = buildJComponent();
        }
        return mJComponent;
    }

    // Returns label for the setting
    abstract String getLabel();

    // Builds the UI component that controls the setting
    abstract JComponent buildJComponent();

    // Helper function that gets numeric values from a FractalSetting array
    static int getNumberAt(FractalSetting[] fractalSettings, int index) {
        return ((FractalSettingNumber) fractalSettings[index]).getValue();
    }

    // Helper function that gets Color objects from a FractalSetting array
    static Color getColorAt(FractalSetting[] fractalSettings, int index) {
        return ((FractalSettingColor) fractalSettings[index]).getValue();
    }
}
