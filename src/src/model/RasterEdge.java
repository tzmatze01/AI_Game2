package model;

/**
 * Created by fschwank on 11.07.17.
 */
public class RasterEdge {
    private final RasterNode source;
    private final RasterNode dest;
    private final int distance;

    public RasterEdge(RasterNode source, RasterNode destination, int distance) {
        this.source = source;
        this.dest = destination;
        this.distance = distance;
    }

    public RasterNode getSource() {
        return this.source;
    }

    public RasterNode getDestination() {
        return this.dest;
    }

    public int getDistance() {
        return this.distance;
    }
}
