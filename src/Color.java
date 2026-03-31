public enum Color {
  RED,
  BLUE,
  GREEN,
  YELLOW;

  @Override
  public String toString() {
    return name().charAt(0) + name().substring(1).toLowerCase();
  }
}
