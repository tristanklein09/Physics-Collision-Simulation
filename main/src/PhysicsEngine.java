import java.util.ArrayList;

//This class handles all the scenarios to be loaded in
public class PhysicsEngine {
    private static PhysicsEngine instance;

    //This means that the frame rate doesn't impact it
    public final double deltaTime = 0.016; //Simulates updating at 60fps - 1/60
    public ArrayList<Body> bodyList = new ArrayList<Body>(); //List to keep track of the bodies

    //World boundaries - where the walls are
    //Maximum x or y coordinates
    double worldXMax = 800;
    double worldYMax = 800;
    //Minimum x or y coordinates
    double worldXMin = 0;
    double worldYMin = 0;

    public enum wallCollisionType {
        NONE,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }


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
        b.velocity = b.velocity.add(b.acceleration.scale(deltaTime)); // v = u + at
        b.position = b.position.add(b.velocity.scale(deltaTime)); // Equivalent of position += velocity * deltaTime
    }

    public void spawnCircleBody(Vector position, Vector velocity, Vector acceleration, double mass, double radius, double restitution) {
        Body ball = new Body(position, velocity, acceleration, mass, radius, restitution);
        bodyList.add(ball);
    }

    //Wall collisions
    public wallCollisionType checkWallCollisions(Body b) {
        double x = b.position.x;
        double y = b.position.y;
        double radius = b.radius;

        //Checking which wall there has been a collision with
        //g.fillOval treats the top left corner as 0,0 so some adjustments need to be made in the logic to compensate for this
        if ((x - radius) <= worldXMin) return wallCollisionType.LEFT;
        if ((x + radius) >= (worldXMax - radius)) return wallCollisionType.RIGHT;
        if ((y - radius) <= worldYMin) return wallCollisionType.TOP;
        if ((y + radius) >= (worldYMax - (2 * radius))) return wallCollisionType.BOTTOM;

        return wallCollisionType.NONE; //Skipped all if statements therefore go to base case
    }

    //Physics for the collision with wall happen
    public void resolveWallCollisions(Body b) {
        // *-1 to invert direction, * restitution to handle the elasticity of the collision
        switch (checkWallCollisions(b)) {
            case NONE:
                break;
            case LEFT, RIGHT:
                b.velocity.x = (b.velocity.x * -1) * b.restitution;
                break;
            case TOP, BOTTOM:
                b.velocity.y = (b.velocity.y * -1) * b.restitution;
        }
    }

    //Where all the updates happen
    public void step() {
        //Update the motion of all bodies
        for (Body b: bodyList) {
            updateMotion(b);
            resolveWallCollisions(b);
        }
    }
}