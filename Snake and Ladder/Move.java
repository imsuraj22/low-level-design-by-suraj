public class Move {
    private Player player;
    private Cell from;
    private Cell to;

    public Move(Player player, Cell from, Cell to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public Player getPlayer() {
        return player;
    }

    public Cell getFromCell() {
        return from;
    }

    public Cell getToCell() {
        return to;
    }
}