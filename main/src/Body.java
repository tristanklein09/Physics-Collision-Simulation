//Currently only a 2D Circular body
public class Body {
    public Vector position;
    public Vector velocity;
    public Vector acceleration;
    public double mass;
    public double radius;
    //How elastic the collision will be (0 = perfectly inelastic, 1 = perfectly elastic)
    public double restitution;

    Body(Vector position, Vector velocity, Vector acceleration, double mass, double radius, double restitution) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        this.radius = radius;
        this.restitution = restitution;
    }

}
