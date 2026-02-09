public class DebugParser {

    public Vector2D parseVector(String text) {

        String[] parts = text.split(",");
        double x = Double.parseDouble(parts[0].trim());
        double y = Double.parseDouble(parts[1].trim());
        return new Vector2D(x, y);
    }

    public double parseDouble(String text) {
        return Double.valueOf(text);
    }
}
