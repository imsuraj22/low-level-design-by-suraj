
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Game {
    private Board board;
    private List<Player> players;
    private Dice dice;
    private List<Player> winningSeq;
    private Scanner sc;
    int n;
    int diceF;
    private Stack<Move> undoStack = new Stack<>();
    private Stack<Move> redoStack = new Stack<>();

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
        // sc.nextLine();
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
                Player p = players.get(i);
                System.out.println(p.getName() + " Press enter to roll the dice");
                String in = sc.nextLine();
                int roll = dice.roll();

                if (roll == diceF)
                    doubleOpp = true;

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
                Move move = new Move(p, p.getCell(), newCell);
                p.setCurrentCell(newCell);
                undoStack.add(move);
                redoStack.clear();
                boolean URdo = false;
                System.out.println(p.getName() + " moved to " + newCell.getIndex());
                if (newCell.getIndex() == n * n) {
                    System.out.println(p.getName() + " wins the game! and got " + (winningSeq.size() + 1) + " rank");
                    System.out.println("Press U to undo, R to redo, any other key to continue");
                    String choice = sc.nextLine().trim();
                    if (choice.equalsIgnoreCase("U")) {
                        undo();
                        URdo = true;
                        i--;
                    } else if (choice.equalsIgnoreCase("R")) {
                        redo();
                        URdo = true;
                        i--;
                    } else {
                        players.remove(i);
                        if (players.size() > 0) {
                            System.out.println("Do you want to stop here or play with other players");
                            System.out.println("Press Y for Yes OR N for No");
                            String input = sc.nextLine();
                            if (input.equalsIgnoreCase("Y")) {

                                i--;
                            } else {
                                won = true;
                            }
                        }

                        winningSeq.add(p);
                        if (won)
                            break;
                    }

                }
                if (URdo == false) {
                    System.out.println("Press U to undo, R to redo, any other key to continue");
                    String choice = sc.nextLine().trim();
                    if (choice.equalsIgnoreCase("U")) {
                        undo();
                        URdo = true;
                        i--;
                    } else if (choice.equalsIgnoreCase("R")) {
                        redo();
                        URdo = true;
                        i--;
                    }
                }
                if (players.contains(p) && doubleOpp == true && URdo == false) {
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

    public void undo() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo!");
            return;
        }
        Move last = undoStack.pop();
        last.getPlayer().setCurrentCell(last.getFromCell());
        redoStack.push(last);
        System.out.println(last.getPlayer().getName() + " move undone.");
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            System.out.println("Nothing to redo!");
            return;
        }
        Move move = redoStack.pop();
        move.getPlayer().setCurrentCell(move.getToCell());
        undoStack.push(move);
        System.out.println(move.getPlayer().getName() + " move redone.");

    }
}
