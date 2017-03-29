/**
 * Main application class. Basically starts the GUI.
 */
public class FractalGenerator {
    private void run() {
        new FractalGeneratorGui();
    }

    public static void main(String... args) {
        new FractalGenerator().run();
    }
}
