public class Board {
    private final Cell[][] board;

    public Board(int n) {
        board = new Cell[n][n];
        int index = 1;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                Cell cell = new Cell(index);
                if ((n - 1 - i) % 2 == 0) {
                    board[i][j] = cell;
                } else {
                    board[i][n - j - 1] = cell;
                }
                index++;
            }
        }
    }

    // --- Snake Methods ---
    public void addSnake(int start, int end) {
        Snake snake = new Snake(start, end);
        int[] startPos = getRowCol(start, board.length);
        board[startPos[0]][startPos[1]].setSnake(snake);
    }

    public void removeSnake(int start) {
        int[] startPos = getRowCol(start, board.length);
        board[startPos[0]][startPos[1]].setSnake(null);
    }

    // --- Ladder Methods ---
    public void addLadder(int start, int end) {
        Ladder ladder = new Ladder(start, end);
        int[] startPos = getRowCol(start, board.length);
        board[startPos[0]][startPos[1]].setLadder(ladder);
    }

    public void removeLadder(int start) {
        int[] startPos = getRowCol(start, board.length);
        board[startPos[0]][startPos[1]].setLadder(null);
    }

    // --- Cell Access Methods ---
    public Cell getCell(int num) {
        int[] pos = getRowCol(num, board.length);
        return board[pos[0]][pos[1]];
    }

    public void setCell(Cell cell, int num) {
        int[] pos = getRowCol(num, board.length);
        board[pos[0]][pos[1]] = cell;
    }

    // --- Helper: Convert number to row/col ---
    private int[] getRowCol(int num, int n) {
        int rowFromBottom = (num - 1) / n;
        int col = (num - 1) % n;
        int row = n - 1 - rowFromBottom;
        if (rowFromBottom % 2 == 1) {
            col = n - 1 - col;
        }
        return new int[] { row, col };
    }
}
