import java.util.ArrayList;

//This class handles all the scenarios to be loaded in
public class PhysicsEngine {
    private static PhysicsEngine instance;
    //Delta time is used as it provides a time interval for the updates, so that all events happen at a set speed
    //This means that the frame rate doesn't impact it
    public final double deltaTime = 0.016; //Simulates updating at 60fps - 1/60
    public boolean simRunning = false;
    public ArrayList<Body> bodyList = new ArrayList<Body>(); //List to keep track of the bodies

    PhysicsEngine() {
        instance = this;
    }

    public static PhysicsEngine getInstance() {
        if (instance == null) { //Handling null
            instance = new PhysicsEngine();
        }
        return instance;
    }

    private void updateMotion(Body b) {
        //Might not work, check
        b.velocity = b.velocity.add(b.acceleration.scale(deltaTime)); // v = u + at
        b.position = b.position.add(b.velocity.scale(deltaTime)); // Equivalent of position += velocity * deltaTime
    }

    public void spawnCircleBody(Vector position, Vector velocity, Vector acceleration, double mass, double radius, double restitution) {
        Body ball = new Body(position, velocity, acceleration, mass, radius, restitution);
        bodyList.add(ball);
    }


    //Where all the updates happen
    public void step() {
        //Update the motion of all bodies
        for (Body b: bodyList) {
            updateMotion(b);
        }

    }
}
