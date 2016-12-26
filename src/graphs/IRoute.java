
// Use this as an interface to your Route class

package graphs;

public interface IRoute {
    public City[] getCities(); // Don't include Gunnison
    public int getLength();   // Should include the journey from and to Gunnison
}
