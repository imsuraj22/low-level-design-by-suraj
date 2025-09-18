
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Board board;
    private List<Player> players;
    private Dice dice;
    private List<Player> winningSeq;
    private Scanner sc;
    int n;
    int diceF;

    public Game(int boardSize) {
        this.board = new Board(boardSize);
        n = boardSize;
        this.players = new ArrayList<>();
        this.winningSeq = new ArrayList<>();
        dice = new Dice(6);
        diceF = 6;
        sc = new Scanner(System.in);

    }

    public void getPlayers() {
        sc.nextLine();
        while (true) {
            System.out.println("Enter the name of the player or press Enter to stop");
            String input = sc.nextLine();
            input = input.trim();
            if (input.isEmpty())
                break;
            Player p = new Player(input);
            p.setCurrentCell(board.getCell(1));
            players.add(p);
        }
    }

    public void fillBoard() {
        System.out.println("We have " + (n * n) + " cells in our board");

        System.out.println("Press Y to add Snakes OR press N to continue");
        String input = sc.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            while (true) {
                System.out.println("Enter the cell number for the Mouth of the snake (or press Enter to stop):");
                Integer start = readIntOrNull();
                if (start == null)
                    break;

                System.out.println("Enter the cell number for the Tail of the snake");
                Integer end = readIntOrNull();
                if (end == null)
                    break;

                board.addSnake(start, end);
                System.out.println("✅ Snake added from " + start + " → " + end);
            }
        }

        System.out.println("Press Y to add Ladders OR press N to continue");
        input = sc.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            while (true) {
                System.out.println("Enter the cell number for the start of the Ladder (or press Enter to stop):");
                Integer start = readIntOrNull();
                if (start == null)
                    break;

                System.out.println("Enter the cell number for the end of the Ladder");
                Integer end = readIntOrNull();
                if (end == null)
                    break;

                board.addLadder(start, end);
                System.out.println("✅ Ladder added from " + start + " → " + end);
            }
        }
    }

    private Integer readIntOrNull() {
        String input = sc.nextLine().trim();

        if (input.isEmpty()) {
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input! Please enter a valid number or press Enter to stop.");
            return readIntOrNull(); // ask again recursively
        }
    }

    public void Play() {
        boolean won = false;
        while (true) {
            for (int i = 0; i < players.size(); i++) {
                boolean doubleOpp = false;
                int roll = dice.roll();

                if (roll == diceF)
                    doubleOpp = true;
                Player p = players.get(i);
                System.out.println(p.getName() + " rolled a " + roll);
                int newpo = p.getCell().getIndex() + roll;
                if (newpo > board.getCell(n * n).getIndex()) {
                    continue;
                }
                Cell newCell = board.getCell(newpo);
                if (newCell.getSnake() != null) {
                    System.out.println("Oops! " + p.getName() + " got bitten by a snake!");
                    newpo = newCell.getSnake().getEnd();
                    newCell = board.getCell(newpo);
                } else if (newCell.getLadder() != null) {
                    System.out.println("Yay! " + p.getName() + " climbed a ladder!");
                    newpo = newCell.getLadder().getEnd();
                    newCell = board.getCell(newpo);
                }
                p.setCurrentCell(newCell);
                System.out.println(p.getName() + " moved to " + newCell.getIndex());
                if (newCell.getIndex() == n * n) {
                    System.out.println(p.getName() + " wins the game! and got " + (winningSeq.size() + 1) + " rank");
                    players.remove(i);
                    if (players.size() > 0) {
                        System.out.println("Do you want to stop here or play with other players");
                        System.out.println("Press Y for Yes OR N for No");
                        String input = sc.next();
                        if (input.equals("Y")) {

                            i--;
                        } else {
                            won = true;
                        }
                    }

                    winningSeq.add(p);
                    if (won)
                        break;
                }
                if (players.contains(p) && doubleOpp == true) {
                    i--;
                }

            }
            if (won)
                break;
            if (players.isEmpty())
                break;
        }

    }

    void printWinners() {
        for (int i = 0; i < winningSeq.size(); i++) {
            System.out.println(winningSeq.get(i).getName() + " got " + (i + 1) + " Rank");
        }
    }
}
