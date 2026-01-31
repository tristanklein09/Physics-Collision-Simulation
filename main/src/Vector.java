public class Vector {
    public double x, y;

    Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //v is the vector being added
    public Vector add(Vector v) {
        Vector result = new Vector((this.x + v.x), (this.y + v.y));
        return result;
    }

    //v is the vector being subtracted
    public Vector subtract(Vector v) {
        Vector result = new Vector((this.x - v.x), (this.x - v.x));
        return result;
    }

    //Multiply by a scalar constant k
    public  Vector scale(double k) {
        Vector result = new Vector((this.x * k), (this.y * k));
        return result;
    }

    //Calculate the dot product
    public double dot(Vector v) {
        double result = (this.x * v.x) + (this.y * v.y);
        return result;
    }

    //Modulus = magnitude
    public double modulus() {
        double result = Math.sqrt((x*x) + (y*y));
        return result;
    }

    //Unit Vector - useful for direction
    public Vector normalise() {
        //Avoid crashing due to division by zero error
        if (this.x == 0 || this.y == 0 ) {
            return new Vector(0, 0);
        }

        double magnitude = this.modulus();
        Vector normalised = new Vector((this.x / magnitude),  (this.y / magnitude));
        return normalised;
    }
}
