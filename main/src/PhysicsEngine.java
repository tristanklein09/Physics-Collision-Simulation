import java.util.ArrayList;

public class PhysicsEngine {
    private static PhysicsEngine instance;

    //Timing
    public final double updateFPS = 120; //Frame rate to simulate
    public final double deltaTime = 1.0 / updateFPS; //At what time interval to update all actions
    public ArrayList<Body> bodyList = new ArrayList<Body>(); //List to keep track of the bodies

    //World boundaries
    public double worldXMax;
    public double worldYMax;
    public double worldXMin = 0;
    public double worldYMin = 0;

    //Physics constants
    public double gravityPXS2 = 981; //Gravity in pixels per second squared (pxs^-2)
    public final double VELOCITY_THRESHOLD = 1; //Threshold for velocity to be considered as zero, to prevent jittering

    public final double overlapCorrectionFactor = 0.2;

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

    public void spawnCircleBody(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double radius, double restitution) {
        Body ball = new Body(position, velocity, acceleration, mass, radius, restitution);
        bodyList.add(ball);
    }

    //Wall collisions
    public wallCollisionType checkWallCollisions(Body b) {
        double currentX = b.position.x;
        double currentY = b.position.y;
        double r = b.radius;

        //Used to checking if there will be a collision between this check and the next check - prevents tunneling
        Vector2D nextPos = b.position.add(b.velocity.scale(deltaTime));
        double nextX = nextPos.x;
        double nextY = nextPos.y;

        //Checking which wall there has been a collision with
        //We also use nextPos to check if there will be a collision between the next delta time, if so treat it as a collision
        //g.fillOval treats the top left corner as 0,0 so some adjustments need to be made in the logic to compensate for this ??
        if (currentX - r <= worldXMin || nextX - r <= worldXMin) return wallCollisionType.LEFT;
        if (currentX + r >= worldXMax || nextX + r >= worldXMax) return wallCollisionType.RIGHT;
        if (currentY - r <= worldYMin || nextY - r <= worldYMin) return wallCollisionType.TOP;
        if (currentY + r >= worldYMax || nextY + r >= worldYMax) return wallCollisionType.BOTTOM;

        return wallCollisionType.NONE; //Skipped all if statements therefore go to base case
    }

    //Physics for the collision with wall happen
    public void resolveWallCollisions(Body b) {
        // *-1 to invert direction, * restitution to handle the elasticity of the collision
        switch (checkWallCollisions(b)) {
            case NONE:
                break;
            case LEFT:
                //Check if the body is moving towards the wall, only change velocity/position if it is to prevent jitter
                if (b.velocity.x < 0) {
                    b.position.x = worldXMin + b.radius;
                    b.velocity.x = (b.velocity.x * -1) * b.restitution;
                }

                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0; //To prevent jittering when the velocity is very low
                    b.acceleration.x = 0; //Stop accelerating in that direction as well, otherwise it will just start moving again
                }
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0;
                    b.acceleration.y = 0;
                }
                break;
            case RIGHT:
                if (b.velocity.x > 0) {
                    b.position.x = worldXMax - b.radius;
                    b.velocity.x = (b.velocity.x * -1) * b.restitution;
                }

                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0;
                    b.acceleration.x = 0;
                }
                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0;
                    b.acceleration.y = 0;
                }
                break;
            case TOP:
                if (b.velocity.y < 0) {
                    b.position.y = worldYMin + b.radius;
                    b.velocity.y = (b.velocity.y * -1) * b.restitution;
                }

                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0;
                    b.acceleration.y = 0;
                }
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0;
                    b.acceleration.x = 0;
                }
                break;
            case BOTTOM:
                if (b.velocity.y > 0) {
                    b.position.y = worldYMax - b.radius;
                    b.velocity.y = (b.velocity.y * -1) * b.restitution;
                }

                if (Math.abs(b.velocity.y) < VELOCITY_THRESHOLD) {
                    b.velocity.y = 0;
                    b.acceleration.y = 0;
                }
                if (Math.abs(b.velocity.x) < VELOCITY_THRESHOLD) {
                    b.velocity.x = 0;
                    b.acceleration.x = 0;
                }
                break;
        }
    }

    //Collision detection between circular bodies
    //TODO: Optimise
    public ArrayList<Pair<Body>> checkCircleCollisions() {
        //Colliding bodies to be processed as pairs due to using linear impulse collision resolution
        ArrayList<Pair<Body>> collidingBodies = new ArrayList<>();

        // Loop through each unique pair exactly once
        for (int i = 0; i < bodyList.size(); i++) {
            Body a = bodyList.get(i);

            //Starting at i+1 means that permutations of already existing pairs aren't considered i.e a,b and b,a
            for (int j = i + 1; j < bodyList.size(); j++) {
                Body b = bodyList.get(j);

                Vector2D vDelta = a.position.subtract(b.position);
                double distance = vDelta.modulus();
                double radiiSum = a.radius + b.radius;

                if (distance <= radiiSum) { // Collision detected
                    collidingBodies.add(new Pair<>(a, b));
                    System.out.println("Collision has occurred");
                }
            }
        }

        return collidingBodies;
    }

    public Vector2D impulseVector(Body a, Body b) {
        //Take the coefficient of restitution to be the smaller of the two values
        double e = Math.min(a.restitution, b.restitution);
        Vector2D vRel = a.velocity.subtract(b.velocity); //Relative velocity

        //To find the collision normal which is the impulseDirection
        Vector2D vDelta = a.position.subtract(b.position);
        Vector2D vNormal = vDelta.normalise();

        double impulseMagnitude = (-(1 + e) * (vRel.dot(vNormal)) / (a.invMass + b.invMass));
        Vector2D impulseDirection = vNormal;

        Vector2D jn = impulseDirection.scale(impulseMagnitude);

        return jn;
    }

    public void resolveCircularCollisions() {
        ArrayList<Pair<Body>> collidingBodies = checkCircleCollisions();

        for (Pair<Body> p : collidingBodies) {
            Body a = p.key();
            Body b = p.value();

            Vector2D jn =  impulseVector(a, b); //Calculate the impulse vector

            //Only apply impulse if the bodies are actually moving towards each other
            Vector2D vRel = a.velocity.subtract(b.velocity);
            Vector2D vARadius = new Vector2D(a.radius, a.radius); //Position vector of the radius from circle top left
            Vector2D vBRadius = new Vector2D(b.radius, b.radius);
            Vector2D vDelta = a.position.subtract(b.position);
            Vector2D vNormal = vDelta.normalise();
            if (vRel.dot(vNormal) > 0) continue;

            //Apply the impulse vector
            a.velocity = a.velocity.add(jn.scale(a.invMass));
            b.velocity = b.velocity.add(jn.scale(-1).scale(b.invMass)); //-jn

            //Positional correction due to overlap
            double penetration = (a.radius + b.radius) - vDelta.modulus(); // vDelta.modulus() is the distance between the centres
            if (penetration <= 0) continue; //No overlap so no correction needed
            double totalInvMass = a.invMass + b.invMass;
            Vector2D correction = vNormal.scale((penetration / totalInvMass) * overlapCorrectionFactor);

            a.position.add(correction.scale(a.invMass)); //Applying the correction
            b.position.subtract(correction.scale(b.invMass));
        }
    }

    //Where all the updates happen
    public void step() {
        //Update the motion of all bodies
        for (Body b: bodyList) {
            updateMotion(b);
            resolveWallCollisions(b);
        }
        resolveCircularCollisions();
    }
}