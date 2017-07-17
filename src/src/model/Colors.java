package model;

/**
 * Created by matthiasdaiber on 16.07.17.
 */
public enum Colors {
    NONE(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    WHITE(4),
    BLACK(5);

    private int value;

    Colors(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
