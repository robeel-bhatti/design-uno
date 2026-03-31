import java.util.*;

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
          System.out.println(
              "Invalid number of players. Please enter a number between 2 and 10.\n");
        }
      } catch (InputMismatchException e) {
        System.out.println("Invalid number of players. Please enter a number between 2 and 10.\n");
      }
    }

    System.out.println("======================\n");

    Game game = new Game();
    game.setPlayerCount(numberOfPlayers);
    game.start();

    while (game.getState() != GameState.FINISHED) {

      Player currentPlayer = game.getCurrentPlayer();
      Card topCard = game.getTopCardOfDiscardPile();
      System.out.println(
          "TOP CARD: " + topCard.getColor().name() + " " + topCard.getDisplay().name());
      game.playTurn();

      if (game.isWinner(currentPlayer)) {
        System.out.println(currentPlayer.getName() + " has won the game!");
      }
    }
    scanner.close();
  }
}
