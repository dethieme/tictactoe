import java.util.Random;

public final class Board {
    private static final int BOARD_WIDTH = 3;

    private final Mark[][] board = new Mark[BOARD_WIDTH][BOARD_WIDTH];

    public Board() {
        this.initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = Mark.EMPTY;
            }
        }
    }

    /**
     * Draws the TicTacToe board in its current play state
     * to the console.
     */
    public void drawBoard() {
        System.out.println("\n   1  2  3");

        for (int i = 0; i < BOARD_WIDTH; i++) {
            System.out.print((i + 1) + "  ");

            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.drawMark(this.board[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    private void drawMark(Mark mark) {
        if (mark == Mark.O) {
            System.out.print("O  ");
        } else if (mark == Mark.X) {
            System.out.print("X  ");
        } else {
            System.out.print("-  ");
        }
    }

    public boolean isGameFinished() {
        return (evaluateGameState() != Mark.EMPTY) || isBoardFull();
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (this.board[i][j] == Mark.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Prints the final game result to console.
     */
    public void printGameResult() {
        Mark gameState = evaluateGameState();

        if (gameState == Mark.EMPTY) {
            System.out.println("Unentschieden");
        } else if (gameState == Mark.X) {
            System.out.println("Du gewinnst!");
        } else {
            System.out.println("Computer gewinnt");
        }
    }

    /**
     * Attempts to make a move for the human player
     * determined by the given row and column.
     *
     * @param row    the row index.
     * @param column the column index.
     * @return true, if the attempted move is valid. False, if not.
     */
    public boolean makeHumanMove(int row, int column) {
        row = row - 1;
        column = column - 1;

        if (this.isValidMove(row, column)) {
            this.board[row][column] = Mark.X;
            return true;
        }

        return false;
    }

    private boolean isValidMove(int row, int column) {
        return row >= 0 && row < BOARD_WIDTH
                && column >= 0 && column < BOARD_WIDTH
                && this.board[row][column] == Mark.EMPTY;
    }

    public void makeComputerMove() {
        Random random = new Random();
        int row, column;

        do {
            row = random.nextInt(BOARD_WIDTH);
            column = random.nextInt(BOARD_WIDTH);
        } while (!isValidMove(row, column));

        this.board[row][column] = Mark.O;
    }

    /**
     * Evaluates the current play state and returns the winner of
     * the game if there is one.
     *
     * @return the symbol of the player, who has won, or {@link Mark#EMPTY} if
     * the game is (currently) a draw.
     */
    private Mark evaluateGameState() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            // Check rows and columns.S
            Mark winner = this.determineWinner(
                    board[i][0].value + board[i][1].value + board[i][2].value, // Sum of line.
                    board[0][i].value + board[1][i].value + board[2][i].value  // Sum of column.
            );

            if (winner != Mark.EMPTY) {
                return winner;
            }
        }

        // Check diagonals.
        return this.determineWinner(
                board[0][0].value + board[1][1].value + board[2][2].value,
                board[0][2].value + board[1][1].value + board[2][0].value);
    }

    private Mark determineWinner(int firstSum, int secondSum) {
        if ((firstSum == -BOARD_WIDTH) || (secondSum == -BOARD_WIDTH)) {
            return Mark.O;
        } else if ((firstSum == BOARD_WIDTH) || (secondSum == BOARD_WIDTH)) {
            return Mark.X;
        }

        // No winner yet.
        return Mark.EMPTY;
    }
}
