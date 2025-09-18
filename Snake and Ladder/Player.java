public class Player {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private Cell currentCell;

    public Player(String name) {
        this.id = idCounter++;
        this.name = name;
        
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Cell getCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell cell) {
        this.currentCell = cell;
    }
}
