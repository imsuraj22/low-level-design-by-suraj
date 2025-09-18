
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the game");
        System.out.println("Enter the size of board");
        String input = sc.next().trim();
        int v = 0;
        try {
            v = Integer.parseInt(input);
            Game game = new Game(v);
            game.fillBoard();
            game.getPlayers();
            game.Play();
            game.printWinners();
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number");
        }

    }
}
