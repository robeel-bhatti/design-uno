import java.util.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        System.out.println("Starting UNO!");
        System.out.println("======================\n");

        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers;

        while (true) {
            System.out.println("Enter number of players (2-10): ");
            try {
                numberOfPlayers = scanner.nextInt();
                if (numberOfPlayers >= 2 && numberOfPlayers <= 10) {
                    break;
                } else {
                    System.out.println("Invalid number of players. Please enter a number between 2 and 10.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid number of players. Please enter a number between 2 and 10.\n");
            }
        }

        System.out.println("======================\n");

        Game game = new Game();
        game.setPlayerCount(numberOfPlayers);
        game.start();

        int currentIndex = 0;
        int direction = 1;

        while (game.getState() != GameState.FINISHED) {

            // get the current player
            Player currentPlayer = game.getPlayers().get(currentIndex);

            // check what the top card is on the discard pile
            // the discard pile should always have a value, but still null check in case new logic gets
            // added that may make the discard pile empty at this point
            Card topCard = game.getDiscardPile().peekLast();

            if (topCard == null) {
                System.out.println("ERROR: Discard Pile was empty when it should have at least one card.");
                game.setState(GameState.FINISHED);
                break;
            }

            System.out.println("TOP CARD: " + topCard.getColor().name() + " " + topCard.getDisplay().name());

            Card playedCard = currentPlayer.playCard(topCard);

            // if the player cannot play any card, the value is null
            if (playedCard == null) {
                System.out.println(currentPlayer.getName() + " cannot play any Card. Drawing from Draw Pile...");
                if (game.getDrawPile().isEmpty()) {
                    System.out.println("No cards left in the Draw Pile. Creating new Draw Pile...");
                    game.setDeck(new ArrayList<>(game.getDiscardPile()));
                    game.clearDiscardPile();
                    game.createDrawPile();
                    game.createDiscardPile();
                }
                currentPlayer.drawCard(game.getDrawPile().pollLast());
            } else {
                // otherwise add their card to the top of the discard pile
                System.out.println(currentPlayer.getName() + " played " + playedCard.getColor().name() + " " + playedCard.getDisplay().name());
                game.getDiscardPile().addLast(playedCard);
            }

            if (currentPlayer.getDeck().isEmpty()) {
                System.out.println(currentPlayer.getName() + " is the winner!");
                game.setState(GameState.FINISHED);
                break;
            }
            currentIndex = game.getNextPlayerIndex(currentIndex, direction);
        }
        scanner.close();
    }
}