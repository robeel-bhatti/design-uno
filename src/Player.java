import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;

    private List<Card> deck = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Card> getDeck() {
        return this.deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    /**
     * If the player can play a card, remove it from their deck.
     * <p></p>
     * In the future, if the return statement is removed after the deck is updated,
     * this will risk a ConcurrentModificationException being thrown.
     * Use the deck's Iterator directly to avoid that exception.
     *
     * @param topCard   The top card on the discard pile
     * @return          The card the player plays, which becomes the new top card on the discard pile.
     */
    public Card playCard(Card topCard) {
        for (Card card : this.deck) {
            if (card.getColor() == topCard.getColor() || card.getDisplay() == topCard.getDisplay()) {
                this.deck.remove(card);
                return card;
            }
        }
        return null;
    }

    public void drawCard(Card card) {
        deck.add(card);
    }
}
