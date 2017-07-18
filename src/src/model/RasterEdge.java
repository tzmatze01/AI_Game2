package model;

/**
 * Created by fschwank on 11.07.17.
 */
public class RasterEdge {
    private final RasterNode source;
    private final RasterNode dest;
    private int weight;

    public RasterEdge(RasterNode source, RasterNode destination) {
        this.source = source;
        this.dest = destination;
    }

    public RasterNode getSource() {
        return this.source;
    }

    public RasterNode getDestination() {
        return this.dest;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
