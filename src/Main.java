import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    System.out.println("Starting UNO!");
    System.out.println("======================\n");

    Scanner scanner = new Scanner(System.in);
    int playerCount = getPlayerCount(scanner);
    scanner.close();

    System.out.println("======================\n");

    Game game = new Game(playerCount);
    game.run();
  }

  private static int getPlayerCount(Scanner scanner) {
    while (true) {
      System.out.println("Enter number of players (2-10): ");
      try {
        int count = scanner.nextInt();
        if (count >= 2 && count <= 10) {
          return count;
        }
      } catch (InputMismatchException e) {
        scanner.next();
      }
      System.out.println("Invalid input. Please enter a number between 2 and 10.\n");
    }
  }
}
