public class Time {

    public long startTime() {
        return System.nanoTime();
    };

    public double endTime(long startTime) {
        return (double) (System.nanoTime() - startTime) / 1000000; //Convert to milliseconds
    };
}
