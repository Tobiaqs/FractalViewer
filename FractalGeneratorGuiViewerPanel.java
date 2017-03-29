import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The panel that shows Fractals.
 */
public class FractalGeneratorGuiViewerPanel extends JPanel implements Fractal.FractalListener, MouseWheelListener, MouseListener, MouseMotionListener {
    private Fractal mFractal;
    private MouseBehaviour mMouseBehaviour = MouseBehaviour.Hand;

    FractalGeneratorGuiViewerPanel() {
        super();

        // Initialize with preferred size
        setPreferredSize(new Dimension(FractalGeneratorGui.PREFERRED_HEIGHT, FractalGeneratorGui.PREFERRED_HEIGHT));

        // Set background
        setBackground(Color.BLACK);

        // Set up listeners for mouse events
        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the fractal on our canvas if we have a fractal
        if (mFractal != null) {
            mFractal.draw(g, getWidth(), getHeight());
        }
    }

    void setFractal(Fractal fractal) {
        if (mFractal != null) {
            mFractal.setFractalListener(null);
        }
        mFractal = fractal;
        mFractal.setFractalListener(this);
        onFractalInvalidated();
    }

    void setMouseBehaviour(MouseBehaviour mouseBehaviour) {
        mMouseBehaviour = mouseBehaviour;
    }

    enum MouseBehaviour {
        ZoomIn, ZoomOut, Select, Hand
    }

    @Override
    public void onFractalInvalidated() {
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mFractal != null) {
            mFractal.zoom(e.getPreciseWheelRotation());
        }
    }

    // Variables used for panning
    private boolean mIsDragging;
    private int mStartPanX;
    private int mStartPanY;
    private int mStartMouseX;
    private int mStartMouseY;

    // Zoom in and out
    @Override
    public void mouseClicked(MouseEvent e) {
        if (mFractal != null) {
            if (mMouseBehaviour == MouseBehaviour.ZoomIn) {
                mFractal.zoom(1);
            } else if (mMouseBehaviour == MouseBehaviour.ZoomOut) {
                mFractal.zoom(-1);
            }
        }
    }

    // Start panning
    @Override
    public void mousePressed(MouseEvent e) {
        if (mMouseBehaviour == MouseBehaviour.Hand && mFractal != null) {
            mIsDragging = true;
            mStartPanX = mFractal.getPanX();
            mStartPanY = mFractal.getPanY();
            mStartMouseX = e.getX();
            mStartMouseY = e.getY();
        }
    }

    // Stop panning
    @Override
    public void mouseReleased(MouseEvent e) {
        mIsDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // Panning
    @Override
    public void mouseDragged(MouseEvent e) {
        if (mMouseBehaviour == MouseBehaviour.Hand && mIsDragging && mFractal != null) {
            mFractal.pan(mStartPanX + e.getX() - mStartMouseX, mStartPanY + e.getY() - mStartMouseY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}
}
