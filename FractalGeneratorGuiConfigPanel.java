import javax.swing.*;
import java.awt.*;

/**
 * Config panel for Fractals.
 */
public class FractalGeneratorGuiConfigPanel extends JPanel {
    FractalGeneratorGuiConfigPanel() {
        super();

        // Set preferred size
        setPreferredSize(new Dimension(200, FractalGeneratorGui.PREFERRED_HEIGHT));

        // We'll use a GridBagLayout
        setLayout(new GridBagLayout());
    }

    void setFractal(Fractal fractal) {
        // Remove all children
        removeAll();

        // Get fractal settings
        FractalSetting[] fractalSettings = fractal.getFractalSettings();

        // Set up the default constraints for children
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Loop over all settings
        for (FractalSetting setting : fractalSettings) {
            // Build a panel with a label and the setting's JComponent
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JLabel label = new JLabel(setting.getLabel());
            panel.add(label, BorderLayout.LINE_START);
            panel.add(setting.getJComponent(), BorderLayout.LINE_END);

            // Add the panel
            add(panel, constraints);

            // Increment gridy position on the constraints object
            constraints.gridy ++;
        }

        // Repaint and revalidate the panel
        repaint();
        revalidate();
    }
}
