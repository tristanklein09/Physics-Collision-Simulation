import javax.swing.*;
import java.awt.*;

//The class that handles the drawing of the objects
public class SimulationPanel extends JPanel {

    private final PhysicsEngine physicsEngine;
    private final Renderer renderer;
    private final double borderFactor = 0.08;

    //Pass through an already existing physicsEngine instance
    public SimulationPanel(PhysicsEngine physicsEngine, Renderer renderer) {
        this.physicsEngine = physicsEngine;
        this.renderer = renderer;

        setDoubleBuffered(true); //Smoother animation
    }

    //Handles drawing the collision objects
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //?
        //Casting to graphics 2D gives better control
        Graphics2D g2D = (Graphics2D) g;

        //Enable anti-aliasing
        if (renderer.antialiasing) {
            g2D.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );
        }

        //Draw all bodies
        for (Body b : physicsEngine.bodyList) {
            drawCircle(g2D, b);
        }
    }

    //Logic for drawing a circle
    private void drawCircle(Graphics2D g2D, Body b) {
        double x = b.position.x;
        double y = b.position.y;
        double r = b.radius;

        //This adjusts for the centre
        int drawX = (int)(x - r);
        int drawY = (int)(y - r);
        int diameter = (int)(r * 2);

        g2D.setColor(Color.RED);
        g2D.fillOval(drawX, drawY, diameter, diameter);

        float borderThickness = (float) (r * borderFactor);
        g2D.setStroke(new BasicStroke(borderThickness));
        g2D.setColor(Color.BLACK);
        g2D.drawOval(drawX, drawY, diameter, diameter);

        if (renderer.showAABB) {
            //AABB
            g2D.setColor(Color.MAGENTA);
            int aabbX = (int) (b.aabbMaxX - b.aabbMinX);
            int aabbY = (int) (b.aabbMaxY - b.aabbMinY);
            g2D.drawRect(drawX, drawY, aabbX, aabbY);
        }

        g2D.setColor(Color.ORANGE);
    }
}