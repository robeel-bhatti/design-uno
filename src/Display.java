import java.util.EnumSet;

public enum Display {
  ZERO("0"),
  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5"),
  SIX("6"),
  SEVEN("7"),
  EIGHT("8"),
  NINE("9"),
  SKIP("Skip"),
  REVERSE("Reverse"),
  DRAW_TWO("Draw Two");

  public static final EnumSet<Display> SPECIAL = EnumSet.of(SKIP, REVERSE, DRAW_TWO);

  private final String label;

  Display(String label) {
    this.label = label;
  }

  @Override
  public String toString() {
    return this.label;
  }
}
