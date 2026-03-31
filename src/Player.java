import java.util.ArrayList;
import java.util.List;

public class Player {

  private final String name;
  private final List<Card> hand = new ArrayList<>();

  public Player(String name) {
    this.name = name;
  }

  public boolean hasEmptyHand() {
    return this.hand.isEmpty();
  }

  /**
   * Finds and removes the first playable card from the player's hand.
   *
   * <p>Uses a for-each loop with an early return after removal. This is safe because the method
   * exits immediately, but would risk ConcurrentModificationException if the loop continued.
   */
  public Card playCard(Card topCard) {
    for (Card card : this.hand) {
      if (card.matches(topCard)) {
        this.hand.remove(card);
        return card;
      }
    }
    return null;
  }

  public void drawCard(Card card) {
    this.hand.add(card);
  }

  @Override
  public String toString() {
    return this.name;
  }
}
