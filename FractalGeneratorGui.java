import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * GUI class for FractalGenerator.
 */
public class FractalGeneratorGui {
    public static final int PREFERRED_HEIGHT = 500;

    private Fractal mFractal;
    private FractalGeneratorGuiViewerPanel mViewerPanel;
    private FractalGeneratorGuiConfigPanel mConfigPanel;

    FractalGeneratorGui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instantiate a JFrame
                JFrame jFrame = new JFrame("Fractal Generator");

                // Make sure our program is shut down when the frame is closed.
                jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

                // Create panel container
                JSplitPane container = new JSplitPane();
                container.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                jFrame.add(container);

                // Create the two panels and add them
                mConfigPanel = new FractalGeneratorGuiConfigPanel();
                container.add(mConfigPanel, JSplitPane.LEFT);

                mViewerPanel = new FractalGeneratorGuiViewerPanel();
                container.add(mViewerPanel, JSplitPane.RIGHT);

                // Create and set menu bar
                JMenuBar menuBar = new JMenuBar();
                jFrame.setJMenuBar(menuBar);

                // Create two menus
                JMenu fileMenu = new JMenu("File");
                fileMenu.setMnemonic(KeyEvent.VK_F);
                menuBar.add(fileMenu);

                JMenu fractalsMenu = new JMenu("Fractals");
                fractalsMenu.setMnemonic(KeyEvent.VK_R);
                menuBar.add(fractalsMenu);

                // Create the buttons for file menu
                JMenuItem loadButton = new JMenuItem("Load settings...");
                loadButton.setMnemonic(KeyEvent.VK_L);
                fileMenu.add(loadButton);

                loadButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        loadFromFile();
                    }
                });

                JMenuItem saveButton = new JMenuItem("Save settings...");
                saveButton.setMnemonic(KeyEvent.VK_S);
                fileMenu.add(saveButton);

                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        saveToFile();
                    }
                });

                JMenuItem exitButton = new JMenuItem("Exit");
                exitButton.setMnemonic(KeyEvent.VK_E);
                fileMenu.add(exitButton);

                // Add a listener to the exit option
                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });

                // Create menu options for all fractal types
                for (Fractal.FractalType type : Fractal.FractalType.values()) {
                    // Create and add item
                    JRadioButtonMenuItem fractalItem = new JRadioButtonMenuItem(Fractal.getFractalTypeName(type));
                    fractalItem.setMnemonic(Fractal.getFractalTypeMnemonic(type));
                    fractalsMenu.add(fractalItem);

                    // Create action listener
                    ActionListener actionListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Select current menu item
                            fractalItem.setSelected(true);

                            // Deselect all other menu items
                            for (Component component : fractalsMenu.getMenuComponents()) {
                                if (component instanceof JRadioButtonMenuItem && component != fractalItem) {
                                    ((JRadioButtonMenuItem) component).setSelected(false);
                                }
                            }

                            // Get the new fractal object from the main program class
                            mFractal = Fractal.getFractal(type);

                            // Set the fractal object in both of the panels
                            mViewerPanel.setFractal(mFractal);
                            mConfigPanel.setFractal(mFractal);
                        }
                    };

                    // Add the action listener to the menu item
                    fractalItem.addActionListener(actionListener);

                    // If the type is equal to Koch, we "click" the menu item to make Koch active
                    if (type == Fractal.FractalType.Koch) {
                        actionListener.actionPerformed(null);
                    }
                }

                // Add a separator in the menu
                fractalsMenu.addSeparator();

                // Create a toggle for the side bar
                JCheckBoxMenuItem sidebarToggle = new JCheckBoxMenuItem("Side bar", true);
                sidebarToggle.setMnemonic(KeyEvent.VK_I);
                fractalsMenu.add(sidebarToggle);

                // Attach a listener to the side bar toggle
                sidebarToggle.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        // Side bar enabled?
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // Remove the viewer panel from the main frame
                            jFrame.remove(mViewerPanel);
                            // Add the viewer panel to the container
                            container.add(mViewerPanel, JSplitPane.RIGHT);

                            // And add the container to the main frame
                            jFrame.add(container);
                        } else {
                            // Remove the viewer panel from the container
                            container.remove(mViewerPanel);

                            // Remove the container from the main frame
                            jFrame.remove(container);

                            // Add the viewer panel to the main frame
                            jFrame.add(mViewerPanel);
                        }

                        // Recalculate everything
                        jFrame.pack();
                    }
                });

                // Create mouse behaviour submenu
                JMenu mouseMenu = new JMenu("Mouse behaviour");
                mouseMenu.setMnemonic(KeyEvent.VK_O);
                fractalsMenu.add(mouseMenu);

                // Select tool
                JRadioButtonMenuItem selectTool = new JRadioButtonMenuItem("Select tool");
                selectTool.setMnemonic(KeyEvent.VK_S);
                mouseMenu.add(selectTool);

                // Zoom in
                JRadioButtonMenuItem zoomIn = new JRadioButtonMenuItem("Zoom-in magnifier");
                zoomIn.setMnemonic(KeyEvent.VK_I);
                mouseMenu.add(zoomIn);

                // Zoom out
                JRadioButtonMenuItem zoomOut = new JRadioButtonMenuItem("Zoom-out magnifier");
                zoomOut.setMnemonic(KeyEvent.VK_O);
                mouseMenu.add(zoomOut);

                // Hand
                JRadioButtonMenuItem handTool = new JRadioButtonMenuItem("Hand tool");
                handTool.setMnemonic(KeyEvent.VK_H);
                handTool.setSelected(true);
                mouseMenu.add(handTool);

                // Put all mouse behaviour items in an array...
                JRadioButtonMenuItem[] mouseBehaviourItems = new JRadioButtonMenuItem[] {
                        selectTool, zoomIn, zoomOut, handTool
                };

                // And iterate over it
                for (JRadioButtonMenuItem mouseBehaviourItem : mouseBehaviourItems) {
                    // Add action listeners to them
                    mouseBehaviourItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Select current menu item
                            mouseBehaviourItem.setSelected(true);

                            // Deselect all other menu items
                            for (Component component : mouseMenu.getMenuComponents()) {
                                if (component instanceof JRadioButtonMenuItem && component != mouseBehaviourItem) {
                                    ((JRadioButtonMenuItem) component).setSelected(false);
                                }
                            }

                            // Set the mouse behaviour on the viewer panel
                            if (mouseBehaviourItem == selectTool) {
                                mViewerPanel.setMouseBehaviour(FractalGeneratorGuiViewerPanel.MouseBehaviour.Select);
                            } else if (mouseBehaviourItem == zoomIn) {
                                mViewerPanel.setMouseBehaviour(FractalGeneratorGuiViewerPanel.MouseBehaviour.ZoomIn);
                            } else if (mouseBehaviourItem == zoomOut) {
                                mViewerPanel.setMouseBehaviour(FractalGeneratorGuiViewerPanel.MouseBehaviour.ZoomOut);
                            } else if (mouseBehaviourItem == handTool) {
                                mViewerPanel.setMouseBehaviour(FractalGeneratorGuiViewerPanel.MouseBehaviour.Hand);
                            }
                        }
                    });
                }

                // Add reset item
                JMenuItem resetButton = new JMenuItem("Reset view");
                resetButton.setMnemonic(KeyEvent.VK_R);
                fractalsMenu.add(resetButton);

                // Add an action listener to the reset button
                resetButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (mFractal != null) {
                            // Reset zoom and pan
                            mFractal.zoom((0.75 - mFractal.getZoom()) * 10);
                            mFractal.pan(0, 0);
                        }
                    }
                });

                // Recalculate everything
                jFrame.pack();

                // Open up the frame
                jFrame.setVisible(true);
            }
        });
    }

    // FileFilter for JFileChooser that only allows .txt files
    private FileFilter mFileFilter = new FileFilter() {
        @Override
        public boolean accept(File f) {
            return f.getAbsolutePath().toLowerCase().endsWith(".txt");
        }

        @Override
        public String getDescription() {
            return "Text Documents (*.txt)";
        }
    };

    private void loadFromFile() {
        // Create file chooser object
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(mFileFilter);

        // Open up the file chooser, and if the user selects a file...
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            // Find the file
            File file = fileChooser.getSelectedFile();
            try {
                // Read the file's contents
                String contents = new Scanner(file, "UTF-8").useDelimiter("\\Z").next();

                // Split by line
                String[] split = contents.split("\n");

                // Get the fractal associated with this file into mFractal
                mFractal = Fractal.getFractal(Fractal.getFractalType(split[0]));

                // Get the FractalSettings
                FractalSetting[] fractalSettings = mFractal.getFractalSettings();

                // Deserialize all lines into their associated FractalSetting
                int pointer = 1;
                for (FractalSetting fractalSetting : fractalSettings) {
                    deserializeFractalSetting(fractalSetting, split[pointer]);
                    pointer ++;
                }

                // Set the fractal object in both of the panels
                mViewerPanel.setFractal(mFractal);
                mConfigPanel.setFractal(mFractal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToFile() {
        // Create file chooser object
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(mFileFilter);

        // Open up the file chooser, and if the user selects a file...
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            // Find the file
            File file = fileChooser.getSelectedFile();

            // Write to it
            try {
                // Open a stream for writing
                FileOutputStream fos = new FileOutputStream(file);

                // StringBuilder for serialization
                StringBuilder builder = new StringBuilder(Fractal.getFractalTypeName(mFractal.getFractalType()));

                // The FractalSettings to serialize
                FractalSetting[] fractalSettings = mFractal.getFractalSettings();

                // Serialize FractalSettings
                for (FractalSetting fractalSetting : fractalSettings) {
                    builder.append('\n').append(serializeFractalSetting(fractalSetting));
                }

                // Write string to FileOutputStream
                fos.write(builder.toString().getBytes("UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Below functions should be moved within the definition of FractalSetting.

    // Function that gets the serialized value from a FractalSetting object.
    private static String serializeFractalSetting(FractalSetting fractalSetting) {
        if (fractalSetting instanceof FractalSettingColor) {
            Color color = ((FractalSettingColor) fractalSetting).getValue();
            return color.getRed() + "," + color.getGreen() + "," + color.getBlue();
        } else if (fractalSetting instanceof FractalSettingNumber) {
            return String.valueOf(((FractalSettingNumber) fractalSetting).getValue());
        }
        return null;
    }

    // Function that sets a serialized value back into a FractalSetting object.
    private static void deserializeFractalSetting(FractalSetting fractalSetting, String serialized) {
        if (fractalSetting instanceof FractalSettingColor) {
            String[] split = serialized.split(",");
            Color color = new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            ((FractalSettingColor) fractalSetting).setInitialValue(color);
        } else if (fractalSetting instanceof FractalSettingNumber) {
            ((FractalSettingNumber) fractalSetting).setInitialValue(Integer.valueOf(serialized));
        }
    }
}
