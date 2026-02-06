import java.util.ArrayList;

//This class handles all the scenarios to be loaded in
public class PhysicsEngine {
    private static PhysicsEngine instance;

    //This means that the frame rate doesn't impact it
    public final double updateFPS = 120; //How many updates per second - what FPS to simulate
    public final double deltaTime = 1.0 / updateFPS; //At what time interval to update to simulate the updateFPS
    public ArrayList<Body> bodyList = new ArrayList<Body>(); //List to keep track of the bodies

    //World boundaries - where the walls are
    //Maximum x or y coordinates
    public double worldXMax;
    public double worldYMax;
    //Minimum x or y coordinates
    public double worldXMin = 0;
    public double worldYMin = 0;

    //Gravity in pixels per second squared (pxs^-2)
    public double gravityPXS2 = 981;

    public final double VELOCITY_THRESHOLD = 5; //Threshold for velocity to be considered as zero, to prevent jittering

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

    public static PhysicsEngine getPEInstance() {
        if (instance == null) { //Handling null
            instance = new PhysicsEngine();
        }
        return instance;
    }

    //Sets the world bounds, allows for the bounds to be scalable and not fixed
    public void setWorldBounds(int width, int height) {
        worldXMax = width;
        worldYMax = height;
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
        double r = b.radius;

        //Checking which wall there has been a collision with
        //g.fillOval treats the top left corner as 0,0 so some adjustments need to be made in the logic to compensate for this ??
        if (x - r <= worldXMin) return wallCollisionType.LEFT;
        if (x + r >= worldXMax) return wallCollisionType.RIGHT;
        if (y - r <= worldYMin) return wallCollisionType.TOP;
        if (y + r >= worldYMax) return wallCollisionType.BOTTOM;

        return wallCollisionType.NONE; //Skipped all if statements therefore go to base case
    }

    //Physics for the collision with wall happen
    public void resolveWallCollisions(Body b) {
        // *-1 to invert direction, * restitution to handle the elasticity of the collision
        switch (checkWallCollisions(b)) {
            case NONE:
                break;
            case LEFT:
                //Check if the body is moving towards the wall
                if (b.velocity.x < 0) b.velocity.x = (b.velocity.x * -1) * b.restitution;
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.x = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.y = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                break;
            case RIGHT:
                if (b.velocity.x > 0) b.velocity.x = (b.velocity.x * -1) * b.restitution;
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.x = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.y = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                break;
            case TOP:
                if (b.velocity.y < 0) b.velocity.y = (b.velocity.y * -1) * b.restitution;
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.y = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.x = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                break;
            case BOTTOM:
                if (b.velocity.y > 0) b.velocity.y = (b.velocity.y * -1) * b.restitution;
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.y = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.x = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                break;
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