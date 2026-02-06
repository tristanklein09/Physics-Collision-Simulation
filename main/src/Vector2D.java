public class Vector2D {
    public double x, y;

    Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //v is the vector being added
    public Vector2D add(Vector2D v) {
        return new Vector2D((this.x + v.x), (this.y + v.y));
    }

    //v is the vector being subtracted
    public Vector2D subtract(Vector2D v) {
        return new Vector2D((this.x - v.x), (this.y - v.y));
    }

    //Multiply by a scalar constant k
    public Vector2D scale(double k) {
        return new Vector2D((this.x * k), (this.y * k));
    }

    //Calculate the dot product
    public double dot(Vector2D v) {
        return (this.x * v.x) + (this.y * v.y);
    }

    //Modulus = magnitude
    public double modulus() {
        return Math.sqrt((x*x) + (y*y));
    }

    //Unit Vector2D - useful for direction
    public Vector2D normalise() {
        //Avoid crashing due to division by zero error caused by zero magnitude
        double magnitude = this.modulus();
        if (magnitude == 0) {
            return new Vector2D(0, 0);
        }

        return new Vector2D((this.x / magnitude),  (this.y / magnitude));
    }
}