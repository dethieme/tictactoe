public final class Board {
    private final int COLUMN_COUNT = 3;

    private final int LINE_COUNT = 3;

    private final Symbol[][] board = new Symbol[LINE_COUNT][COLUMN_COUNT];
    private int spielTiefe = 0;
    private int bestMove = 0;

    public Board() {
        for (int i = 0; i < LINE_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                board[i][j] = Symbol.EMPTY;
            }
        }
    }

    /**
     * Draws the TicTacToe board in its current play state
     * to the console.
     */
    public void drawBoard() {
        System.out.println("\n   1   2   3");

        for (int i = 0; i < LINE_COUNT; i++) {
            System.out.print((i + 1) + "  ");

            for (int j = 0; j < COLUMN_COUNT; j++) {
                if (this.board[i][j] == Symbol.O) {
                    System.out.print("O  ");
                } else if (board[i][j] == Symbol.X) {
                    System.out.print("X  ");
                } else {
                    System.out.print("-  ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }

    public boolean isGameFinished() {
        return (determineGameState() != Symbol.EMPTY) || isBoardFull();
    }

    private boolean isBoardFull() {
        return countNonEmptySymbols() == COLUMN_COUNT * LINE_COUNT;
    }

    /**
     * Prints the final game result to console.
     */
    public void printGameResult() {
        Symbol gameState = determineGameState();

        if (gameState == Symbol.EMPTY) {
            System.out.println("Unentschieden");
        } else if (gameState == Symbol.X) {
            System.out.println("Du gewinnst!");
        } else {
            System.out.println("Computer gewinnt");
        }
    }

    // X ist am Zug, bester Zug wird in bestMove gepseichert
    public int minmaxX(int tiefe) {
        // vielleicht ist das Spiel schon fertig?
        Symbol gameState = determineGameState();

        if (gameState != Symbol.EMPTY) {
            return gameState.value;
        }

        if (isBoardFull()) {
            return 0;
        }

        int max = -5;
        int[] moves = genMoves();

        for (int j : moves) {
            // Make temporary move.
            this.board[j / 10][j % 10] = Symbol.X;
            int wert = minmaxO(tiefe + 1);

            if (wert > max) {
                max = wert;
                if (tiefe == spielTiefe)
                    bestMove = j;
            }

            // Take back temporary move.
            this.board[j / 10][j % 10] = Symbol.EMPTY;
        }

        return max;
    }

    // O ist am Zug, bester Zug wird in bestMove gepseichert
    public int minmaxO(int tiefe) {
        // vielleicht ist das Spiel schon fertig?
        Symbol eval = determineGameState();

        if (eval != Symbol.EMPTY) {
            return eval.value;
        }

        if (isBoardFull()) {
            return 0;
        }

        int min = 5;
        int[] zuege = genMoves();

        for (int j : zuege) {
            this.board[j / 10][j % 10] = Symbol.O;
            int wert = minmaxX(tiefe + 1);

            if (wert < min) {
                min = wert;

                if (tiefe == this.spielTiefe) {
                    this.bestMove = j;
                }
            }

            // Take back temporary move.
            this.board[j / 10][j % 10] = Symbol.EMPTY;
        }

        return min;
    }

    /**
     * Attempts to make a move for the human player
     * determined by the given coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return true, if the attempted move is valid. False, if not.
     */
    public boolean attemptHumanPlayerMove(int x, int y) {
        x = x - 1;
        y = y - 1;

        if (isCoordinateValid(x) && isCoordinateValid(y) && (board[x][y] == Symbol.EMPTY)) {
            this.board[x][y] = Symbol.X;
            this.spielTiefe++;
            return true;
        }

        return false;
    }

    private boolean isCoordinateValid(int coordinate) {
        return (coordinate >= 0) && (coordinate < 3);
    }

    // Computer darf einen Zug machen
    public void compZug() {
        minmaxO(this.spielTiefe);
        this.board[bestMove / 10][bestMove % 10] = Symbol.O;
        this.spielTiefe++;
    }

    private int countNonEmptySymbols() {
        int count = 0;

        for (int i = 0; i < LINE_COUNT; i++) {
            for (int j = 0; j < COLUMN_COUNT; j++) {
                if (this.board[i][j] != Symbol.EMPTY) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Generates a list of open positions with x * 10 + y
     *
     * @return
     */
    private int[] genMoves() {
        // speichere die Züge
        int[] moves = new int[9 - countNonEmptySymbols()];  // wieviele Züge gibt es?
        int moveCount = 0;

        for (int i = 0; i < LINE_COUNT; i++)
            for (int j = 0; j < COLUMN_COUNT; j++)
                if (this.board[i][j] == Symbol.EMPTY)
                    moves[moveCount++] = i * 10 + j;

        return moves;
    }

    /**
     * Determines the current play state.
     * -1 O-gewinnt, 1 X-gewinnt, sonst 0
     *
     * @return
     */
    private Symbol determineGameState() {
        for (int i = 0; i < LINE_COUNT; i++) {
            int lineSum = board[i][0].value + board[i][1].value + board[i][2].value;
            int columnSum = board[0][i].value + board[1][i].value + board[2][i].value;

            if ((lineSum == -3) || (columnSum == -3)) {
                return Symbol.O;
            } else if ((lineSum == 3) || (columnSum == 3)) {
                return Symbol.X;
            }
        }

        int diagonalSum;
        int columnSum;

        // Evaluate diagonal from upper left corner to lower right corner.
        diagonalSum = board[0][0].value + board[1][1].value + board[2][2].value;

        // Evaluate diagonal lower left corner to upper right corner.
        columnSum = board[0][2].value + board[1][1].value + board[2][0].value;

        if ((diagonalSum == -3) || (columnSum == -3)) {
            return Symbol.O;
        } else if ((diagonalSum == 3) || (columnSum == 3)) {
            return Symbol.X;
        }

        // Game state is (still) a draw.
        return Symbol.EMPTY;
    }
}
