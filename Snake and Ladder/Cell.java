public class Cell {
    private int index;
    private Snake snake;
    private Ladder ladder;

    public Cell(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Ladder getLadder() {
        return ladder;
    }

    public void setLadder(Ladder ladder) {
        this.ladder = ladder;
    }

    public boolean hasSnake() {
        return snake != null;
    }

    public boolean hasLadder() {
        return ladder != null;
    }

    public int getEndPosition() {
        if (hasSnake())
            return snake.end;
        if (hasLadder())
            return ladder.end;
        return -1;
    }

}
