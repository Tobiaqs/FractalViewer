import java.awt.*;

/**
 * Created by me on 10/31/2016.
 */
public class FractalJulia extends Fractal {
    FractalJulia() {
        super(FractalType.Julia);
    }

    @Override
    void draw(Graphics g, int width, int height) {

    }

    @Override
    FractalSetting[] buildFractalSettings() {
        return new FractalSetting[0];
    }
}
