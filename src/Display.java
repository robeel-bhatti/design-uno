import java.util.EnumSet;

public enum Display {

    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    SKIP,
    REVERSE,
    DRAW_TWO;

    public static final EnumSet<Display> specialVals = EnumSet.of(SKIP, REVERSE, DRAW_TWO);

}
