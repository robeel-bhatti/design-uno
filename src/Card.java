public class Card {

    private Color color;
    private Display display;

    public Card(Color color, Display display) {
        this.color = color;
        this.display = display;
    }

    public Color getColor() {
        return color;
    }

    public Display getDisplay() {
        return display;
    }
}
