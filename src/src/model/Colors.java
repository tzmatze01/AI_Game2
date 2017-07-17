package model;

/**
 * Created by matthiasdaiber on 17.07.17.
 */
public enum Colors {

    NONE(0),
    RED(1),
    GREEN(2),
    BLUE(3),
    BLACK(4),
    WHITE(5);

    private int value;

    private Colors(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return this.value;
    }
}
