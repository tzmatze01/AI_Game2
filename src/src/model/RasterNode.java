package model;

/**
 * Created by fschwank on 11.07.17.
 */
public class RasterNode {
    private final int id;
    private int middleX;
    private int middleY;
    private int meanColor;
    private int numberOfBotsInRaster;

    public RasterNode(int id, int middleX, int middleY) {
        this.id = id;
        this.middleX = middleX;
        this.middleY = middleY;
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

    public void setMeanColor(int meanColor) {
        this.meanColor = meanColor;
    }

    public int getNumberOfBotsInRaster() {
        return this.numberOfBotsInRaster;
    }

    public void setNumberOfBotsInRaster(int numberOfBotsInRaster) {
        this.numberOfBotsInRaster = numberOfBotsInRaster;
    }
}
