
package graphs;

public class Road {
    public int length;
    public double speed;
    public String name;
    public City start;
    public City end;
    public Road(String name, City start, City end, int length, int time) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.length = length;
        this.speed = (double) length / ((double) time);
    }
    @Override
    public String toString() {
        return name + " (" + start.name + " " + end.name + ")";
    }
}
