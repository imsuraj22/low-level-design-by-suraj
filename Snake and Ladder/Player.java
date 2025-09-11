public class Player {
    private static int idCounter = 1;
    private final int id;
    private String name;
    private int position;

    public Player(String name) {
        this.id = idCounter++;
        this.name = name;
        this.position = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
