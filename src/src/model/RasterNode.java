package model;

import logic.Pathfinder;
import strategy.Strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fschwank on 11.07.17.
 */
public class RasterNode implements Comparable {

    private final int id;
    private int middleX;
    private int middleY;
    private int meanColor;
    private int numberOfBotsInRaster;

    private List<RasterEdge> wayPoints;

    // The previous node of this one on the currently calculated path
    private RasterNode previousNode;

    // A-Star algorithm: Calculated cost from start-node to this node
    private int gCosts;

    // A-Star algorithm: Calculated cost from this node to end-node
    private int hCosts;

    public RasterNode(int id, int middleX, int middleY, int meanColor)
    {
        this.id = id;
        this.middleX = middleX;
        this.middleY = middleY;
        this.meanColor = meanColor;

        this.wayPoints = new ArrayList<>();
    }
    /** Getter & Setter begin **/

    public int getId() {
        return this.id;
    }

    public int getMiddleX() {
        return this.middleX;
    }

    public int getMiddleY() {
        return this.middleY;
    }

    public int getMeanColor() {
        return this.meanColor;
    }

    public RasterNode getPreviousNode() { return this.previousNode; }

    public int getgCosts() { return this.gCosts; }

    public int gethCosts() { return this.hCosts; }

    public int getfCosts() { return this.gCosts + this.hCosts; }

    public List<RasterEdge> getWayPoints() {
        return this.wayPoints;
    }

    public int getNumberOfBotsInRaster() {
        return this.numberOfBotsInRaster;
    }

    public void setMeanColor(int meanColor) {
        this.meanColor = meanColor;
    }

    public void setgCosts(int gCosts) { this.gCosts = gCosts; }

    public void setgCosts(RasterNode previousNode) {
        this.gCosts = previousNode.getgCosts() + Strategy.BASIC_MOVEMENT_COST;
    }

    public void sethCosts(RasterNode targetNode, int calculatedCosts) {
        this.sethCosts((getAbsolute(this.getMiddleX() - targetNode.getMiddleX())
                + getAbsolute(this.getMiddleY() - targetNode.getMiddleY()))
                * calculatedCosts);
    }

    private int getAbsolute(int a) {
        return a > 0 ? a : -a;
    }

    public void sethCosts(int hCosts) { this.hCosts = hCosts; }

    public void setNumberOfBotsInRaster(int numberOfBotsInRaster) {
        this.numberOfBotsInRaster = numberOfBotsInRaster;
    }

    public void setPreviousNode(RasterNode previousNode) { this.previousNode = previousNode; }

    public void setWayPoints(List<RasterEdge> wayPoints) {
        this.wayPoints = wayPoints;
    }

    /** Getter & Setter end **/

    public void addWayPoint(RasterEdge wayPoint) {
        this.wayPoints.add(wayPoint);
    }

    public int calculateGCosts() {
        return 0;
    }

    public int calculateGCosts(RasterNode previousNode) {
        return 0;
    }

    public int calculateHCosts() {
        return 0;
    }

    public int calculateHCosts(RasterNode previousNode) {
        return 0;
    }

    @Override
    public int compareTo(Object o) {

        int botsOfNode = ((RasterNode)o).getNumberOfBotsInRaster();


        if(numberOfBotsInRaster > botsOfNode)
            return 1;
        if(numberOfBotsInRaster < botsOfNode)
            return -1;

        return 0;
    }
}
