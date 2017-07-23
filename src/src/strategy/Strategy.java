package strategy;

import model.RasterNode;

/**
 * Created by FSCHWANK on 23.07.2017.
 */
public abstract class Strategy {

    public static final int BASIC_MOVEMENT_COST = 10;

    private int ownColor;

    public Strategy() { }

    public Strategy(int ownColor) {
        this.ownColor = ownColor;
    }

    public int getOwnColor() {
        return this.ownColor;
    }

    public int calculateCostOfField(RasterNode node){
        return 0;
    }
}
