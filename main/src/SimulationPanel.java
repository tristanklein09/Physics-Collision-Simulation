import javax.swing.*;
import java.awt.*;

//TODO: ADD ANTI-ALIASING

//The class that handles the drawing of the objects
public class SimulationPanel extends JPanel {

    private final PhysicsEngine physicsEngine;

    //Pass through an already existing physicsEngine instance
    public SimulationPanel(PhysicsEngine physicsEngine) {
        this.physicsEngine = physicsEngine;

        setDoubleBuffered(true); //Smoother animation
    }

    //Might not work
    //handles drawing the collision objects
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //?

        //Casting to graphics 2D gives better control
        Graphics2D g2D = (Graphics2D) g;

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

        int drawX = (int)(x - r);
        int drawY = (int)(y - r);
        int diameter = (int)(r * 2);

        //Drawing the circle
        g2D.setColor(Color.RED);
        g2D.fillOval(drawX, drawY, diameter, diameter);
    }
}
