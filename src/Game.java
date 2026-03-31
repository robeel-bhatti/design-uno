import java.util.*;

public class Game {

  private final List<Card> deck = new ArrayList<>();

  private final Deque<Card> drawPile = new ArrayDeque<>();

  private final Deque<Card> discardPile = new ArrayDeque<>();

  private final List<Player> players = new ArrayList<>();

  private int playerCount;

  private int currentIndex = 0;

  private int currentDirection = 1;

  private GameState state;

  public Game() {
    this.state = GameState.STARTING;
  }

  public void start() {
    this.createPlayers();
    this.createDeck();
    this.createDrawPile();
    this.distributeCards();
    this.createDiscardPile();
    this.state = GameState.IN_PROGRESS;
  }

  public Card getTopCardOfDiscardPile() {
    Card card = this.discardPile.peekLast();
    if (card == null) {
      this.state = GameState.FINISHED;
      throw new RuntimeException("GAME ERROR: A null card was found in the discard pile.");
    }
    return card;
  }

  public Player getCurrentPlayer() {
    return this.players.get(this.currentIndex);
  }

  public void setPlayerCount(int playerCount) {
    this.playerCount = playerCount;
  }

  public void playTurn() {
    Player player = this.getCurrentPlayer();
    Card topCard = this.getTopCardOfDiscardPile();
    Card playedCard = player.playCard(topCard);
    if (playedCard == null) {
      System.out.println(player.getName() + " cannot play any Card. Drawing from Draw Pile...");
      this.checkDrawPile();
      player.drawCard(this.drawPile.pollLast());

    } else {
      System.out.println(
          player.getName()
              + " played "
              + playedCard.getColor().name()
              + " "
              + playedCard.getDisplay().name());
      this.discardPile.addLast(playedCard);
      if (Display.specialVals.contains(playedCard.getDisplay())) {
        this.handleSpecialCards(playedCard);
      }
    }
    this.updateCurrentIndex();
  }

  private void handleSpecialCards(Card playedCard) {
    switch (playedCard.getDisplay()) {
      case SKIP -> this.updateCurrentIndex();
      case REVERSE -> this.currentDirection *= -1;
      case DRAW_TWO -> {
        this.updateCurrentIndex();
        Player nextPlayer = this.getCurrentPlayer();
        System.out.println(nextPlayer.getName() + " draws 2 cards and is skipped!");
        for (int i = 0; i < 2; i++) {
          this.checkDrawPile();
          nextPlayer.drawCard(this.drawPile.pollLast());
        }
      }
    }
  }

  public boolean isWinner(Player player) {
    if (player.getDeck().isEmpty()) {
      this.state = GameState.FINISHED;
      return true;
    }
    return false;
  }

  /**
   * Generates the initial draw pile with the following rules: generate 19 numbered cards per color
   * each color has 1 card for 0 each color has 2 cards each for 1-9 each color has 2 cards each for
   * DRAW 2, SKIP, and REVERSE
   */
  private void createDeck() {

    for (Color color : Color.values()) {
      for (Display display : Display.values()) {
        this.deck.add(new Card(color, display));
        if (display != Display.ZERO) {
          this.deck.add(new Card(color, display));
        }
      }
    }

    if (this.deck.size() != 100) {
      throw new RuntimeException("GAME ERROR: Deck size is not 100.");
    }
  }

  private void createDrawPile() {
    Collections.shuffle(this.deck);
    this.getDrawPile().addAll(this.deck);
  }

  private void createDiscardPile() {
    Iterator<Card> iterator = this.drawPile.descendingIterator();
    if (!iterator.hasNext()) {
      throw new RuntimeException("GAME ERROR: The draw pile is empty.");
    }
    while (iterator.hasNext()) {
      Card card = iterator.next();
      if (card == null) {
        throw new RuntimeException("GAME ERROR: Null value in the draw pile.");
      } else if (!Display.specialVals.contains(card.getDisplay())) {
        this.discardPile.add(card);
        iterator.remove();
        return;
      }
    }
  }

  private void distributeCards() {
    final int maxPerPlayer = 7;

    for (Player player : this.players) {
      List<Card> playerDeck = new ArrayList<>();
      for (int i = 0; i < maxPerPlayer; i++) {
        Card card = this.drawPile.pollFirst();
        if (card == null) {
          throw new RuntimeException("Something went wrong with the card distribution");
        }
        playerDeck.add(card);
      }
      player.setDeck(playerDeck);
    }
  }

  private void createPlayers() {
    for (int i = 0; i < this.playerCount; i++) {
      String name = "Player " + (i + 1);
      this.players.add(new Player(name));
    }
  }

  private Deque<Card> getDrawPile() {
    return drawPile;
  }

  private void checkDrawPile() {
    if (this.getDrawPile().isEmpty()) {
      System.out.println("\nGAME INFO: No cards left in the Draw Pile. Creating new Draw Pile\n");
      this.setDeck();
      this.clearDiscardPile();
      this.createDrawPile();
      this.createDiscardPile();
    }
  }

  private void setDeck() {
    this.deck.clear();
    this.deck.addAll(this.discardPile);
  }

  private void clearDiscardPile() {
    this.discardPile.clear();
  }

  public GameState getState() {
    return this.state;
  }

  private void updateCurrentIndex() {
    this.currentIndex =
        Math.floorMod(this.currentIndex + this.currentDirection, this.players.size());
  }
}
