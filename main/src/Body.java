//Currently only a 2D Circular body
public class Body {
    public Vector2D position; //Position to the centre
    public Vector2D velocity;
    public Vector2D acceleration;
    public double mass;
    public double invMass;
    public double radius;
    public double restitution; //How elastic the collision will be (0 = perfectly inelastic, 1 = perfectly elastic)

    //AABB
    public double aabbMinX;
    public double aabbMaxX;
    public double aabbMinY;
    public double aabbMaxY;

    Body(Vector2D position, Vector2D velocity, Vector2D acceleration, double mass, double radius, double restitution) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;

        this.mass = mass;
        if (mass <= 0) throw new IllegalArgumentException("Mass must be greater than zero.");
        invMass = 1 / this.mass;

        this.radius = radius;
        this.restitution = restitution;
    }

}