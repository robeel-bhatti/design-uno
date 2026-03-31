public record Card(Color color, Display display) {

  public boolean matches(Card other) {
    return this.color == other.color || this.display == other.display;
  }

  @Override
  public String toString() {
    return this.color + " " + this.display;
  }
}
