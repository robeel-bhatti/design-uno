import java.util.*;

public class Game {

    private List<Card> deck = new ArrayList<>();

    private final Deque<Card> drawPile = new ArrayDeque<>();

    private final Deque<Card> discardPile = new ArrayDeque<>();

    private final List<Player> players = new ArrayList<>();

    private int playerCount;

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

  /**
   * Generates the initial draw pile with the following rules:
   * generate 19 numbered cards per color
   * each color has 1 card for 0
   * each color has 2 cards each for 1-9
   * each color has 2 cards each for DRAW 2, SKIP, and REVERSE
   */
  private void createDeck() {

        for (Color color : Color.values()) {
            for (Display display : Display.values()) {
                Card cardOne = new Card(color, display);
                this.deck.add(cardOne);

                if (display != Display.ZERO) {
                    Card cardTwo = new Card(color, display);
                    this.deck.add(cardTwo);
                }
            }
        }

        if (this.deck.size() != 100) {
            throw new RuntimeException("ERROR: Deck size is not 100.");
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

    public void createDrawPile() {
        Collections.shuffle(this.deck);
        this.getDrawPile().addAll(this.deck);
    }

    public void createDiscardPile() {
      Iterator<Card> iterator = this.drawPile.descendingIterator();
      if (!iterator.hasNext()) {
          throw new RuntimeException("ERROR: The draw pile is empty.");
      }
      while (iterator.hasNext()) {
          Card card = iterator.next();
          if (card == null) {
              throw new RuntimeException("ERROR: Null value in the draw pile.");
          } else if (!Display.specialVals.contains(card.getDisplay())) {
              this.discardPile.add(card);
              iterator.remove();
              return;
          }
      }
    }

    public void createPlayers() {
        for (int i = 0; i < this.playerCount; i++) {
            String name = "Player " + (i + 1);
            this.players.add(new Player(name));
        }
    }

    public Deque<Card> getDrawPile() {
        return drawPile;
    }

    public Deque<Card> getDiscardPile() {
        return discardPile;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public int getNextPlayerIndex(int index, int direction) {
      return Math.floorMod(index + direction, this.players.size());
    }

    public void clearDiscardPile() {
      this.discardPile.clear();
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
