package model;

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

    public RasterNode(int id, int middleX, int middleY, int meanColor)
    {
        this.id = id;
        this.middleX = middleX;
        this.middleY = middleY;
        this.meanColor = meanColor;

        this.wayPoints = new ArrayList<>();
    }

    public void addWayPoint(RasterEdge wayPoint) {
        this.wayPoints.add(wayPoint);
    }
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

    public List<RasterEdge> getWayPoints() {
        return this.wayPoints;
    }

    public int getNumberOfBotsInRaster() {
        return this.numberOfBotsInRaster;
    }

    public void setMeanColor(int meanColor) {
        this.meanColor = meanColor;
    }

    public void setNumberOfBotsInRaster(int numberOfBotsInRaster) {
        this.numberOfBotsInRaster = numberOfBotsInRaster;
    }

    public void setWayPoints(List<RasterEdge> wayPoints) {
        this.wayPoints = wayPoints;
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
