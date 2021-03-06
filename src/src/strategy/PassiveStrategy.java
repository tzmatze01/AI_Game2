package strategy;

import model.Colors;
import model.RasterNode;

/**
 * Created by FSCHWANK on 23.07.2017.
 */
public class PassiveStrategy extends Strategy {

    private final int COST_BLACK_FIELD = 999;
    private final int COST_OWN_FIELD = 200;

    private final int COST_WHITE_FIELD = 100;
    private final int COST_ENEMY_FIELD = 150;
    private final int COST_LEADER_FIELD = 150;

    public int calculateCostOfField(RasterNode node) {
        int costs = 0;

        if (node.getMeanColor() == Colors.WHITE.getValue()) {
            costs = COST_WHITE_FIELD;
        }

        if (node.getMeanColor() == Colors.BLACK.getValue()) {
            costs = COST_BLACK_FIELD;
        }

        if (node.getMeanColor() == getOwnColor()) {
            costs = COST_OWN_FIELD;
        }

        if (node.getMeanColor() != getOwnColor()) {
            costs = COST_ENEMY_FIELD;
        }

        costs += costs + BASIC_MOVEMENT_COST;

        return costs;
    }
}
