package model;

/**
 * Created by matthiasdaiber on 16.07.17.
 */
public enum Colors {
    NONE(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    BLACK(4),
    WHITE(5);

    private Colors(int value)
    {
        this.value = value;
    }

    private int value;

    public int getValue() {
        return this.value;
    }
}