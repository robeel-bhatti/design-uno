import java.util.*;

public class Game {

  private static final int HAND_SIZE = 7;

  private final Deque<Card> drawPile = new ArrayDeque<>();
  private final Deque<Card> discardPile = new ArrayDeque<>();
  private final List<Player> players = new ArrayList<>();

  private int currentIndex = 0;
  private int direction = 1;
  private GameState state = GameState.STARTING;

  public Game(int playerCount) {
    for (int i = 0; i < playerCount; i++) {
      this.players.add(new Player("Player " + (i + 1)));
    }
  }

  public void run() {
    this.setup();

    while (this.state == GameState.IN_PROGRESS) {
      this.playTurn();
    }
  }

  private void setup() {
    List<Card> deck = buildDeck();
    Collections.shuffle(deck);
    this.drawPile.addAll(deck);
    this.dealHands();
    this.placeFirstCard();
    this.state = GameState.IN_PROGRESS;
  }

  private void playTurn() {
    Player player = this.players.get(this.currentIndex);
    Card topCard = this.discardPile.peekLast();

    System.out.println("TOP CARD: " + topCard);

    Card played = player.playCard(topCard);

    if (played == null) {
      System.out.println(player + " cannot play. Drawing from Draw Pile...");
      this.refillDrawPileIfEmpty();
      player.drawCard(this.drawPile.pollLast());
    } else {
      System.out.println(player + " played " + played);
      this.discardPile.addLast(played);

      if (player.hasEmptyHand()) {
        System.out.println(player + " has won the game!");
        this.state = GameState.FINISHED;
        return;
      }

      this.handleSpecialCard(played);
    }

    this.advanceTurn();
  }

  private void handleSpecialCard(Card card) {
    switch (card.display()) {
      case SKIP -> this.advanceTurn();
      case REVERSE -> this.direction *= -1;
      case DRAW_TWO -> {
        this.advanceTurn();
        Player next = this.players.get(this.currentIndex);
        System.out.println(next + " draws 2 cards and is skipped!");
        for (int i = 0; i < 2; i++) {
          this.refillDrawPileIfEmpty();
          next.drawCard(this.drawPile.pollLast());
        }
      }
    }
  }

  private void advanceTurn() {
    this.currentIndex = Math.floorMod(this.currentIndex + this.direction, this.players.size());
  }

  private static List<Card> buildDeck() {
    List<Card> deck = new ArrayList<>();
    for (Color color : Color.values()) {
      for (Display display : Display.values()) {
        deck.add(new Card(color, display));
        if (display != Display.ZERO) {
          deck.add(new Card(color, display));
        }
      }
    }
    if (deck.size() != 100) {
      throw new RuntimeException("Deck size is " + deck.size() + ", expected 100.");
    }
    return deck;
  }

  private void dealHands() {
    for (Player player : this.players) {
      for (int i = 0; i < HAND_SIZE; i++) {
        player.drawCard(this.drawPile.pollFirst());
      }
    }
  }

  /**
   * Places the first non-special card from the top of the draw pile onto the discard pile.
   */
  private void placeFirstCard() {
    Iterator<Card> it = this.drawPile.descendingIterator();
    while (it.hasNext()) {
      Card card = it.next();
      if (!Display.SPECIAL.contains(card.display())) {
        this.discardPile.add(card);
        it.remove();
        return;
      }
    }
    throw new RuntimeException("No non-special card found to start the discard pile.");
  }

  private void refillDrawPileIfEmpty() {
    if (!this.drawPile.isEmpty()) {
      return;
    }
    System.out.println("\nDraw Pile empty. Reshuffling discard pile...\n");
    Card top = this.discardPile.pollLast();
    List<Card> cards = new ArrayList<>(this.discardPile);
    this.discardPile.clear();
    this.discardPile.add(top);
    Collections.shuffle(cards);
    this.drawPile.addAll(cards);
  }
}
