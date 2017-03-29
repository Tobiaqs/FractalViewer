import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * An implementation of FractalSetting, is a number.
 */
public class FractalSettingNumber extends FractalSetting {
    private final String mLabel;
    private int mValue;
    private final SpinnerNumberModel mSpinnerModel;

    FractalSettingNumber(Fractal fractal, SpinnerNumberModel spinnerModel, String label) {
        super(fractal);

        mLabel = label;
        mSpinnerModel = spinnerModel;
        mValue = (int) spinnerModel.getValue();
    }

    public int getValue() {
        return mValue;
    }

    public void setInitialValue(int value) {
        mSpinnerModel.setValue(value);
        mValue = value;
    }

    @Override
    String getLabel() {
        return mLabel;
    }

    @Override
    JComponent buildJComponent() {
        JSpinner spinner = new JSpinner(mSpinnerModel);
        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                mValue = (int) spinner.getValue();
                mFractal.invalidate();
            }
        });
        return spinner;
    }
}
