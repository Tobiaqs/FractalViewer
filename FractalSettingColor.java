import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An implementation of FractalSetting, is a Color.
 */
public class FractalSettingColor extends FractalSetting {
    private final String mLabel;
    private Color mColor;
    FractalSettingColor(Fractal fractal, Color defaultColor, String label) {
        super(fractal);

        mColor = defaultColor;
        mLabel = label;
    }

    public Color getValue() {
        return mColor;
    }

    public void setInitialValue(Color color) {
        mColor = color;
    }

    @Override
    String getLabel() {
        return mLabel;
    }

    @Override
    JComponent buildJComponent() {
        final JButton button = new JButton();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mColor = JColorChooser.showDialog(null, "Choose a color", mColor);
                button.setBackground(mColor);
                mFractal.invalidate();
            }
        });
        button.setBackground(mColor);
        return button;
    }
}
