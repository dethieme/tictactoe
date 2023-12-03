import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public final class Board {
    private static final int BOARD_WIDTH = 3;

    private final Mark[][] board = new Mark[BOARD_WIDTH][BOARD_WIDTH];

    public Board() {
        this.initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.board[i][j] = Mark.EMPTY;
            }
        }
    }

    public void startGameLoop() {
        System.out.println("Herzlich Willkommen. Dein Symbol ist das X.");
        this.drawBoardToConsole();

        boolean isHumanTurn = true;
        Scanner scanner = new Scanner(System.in);

        while (!this.isGameFinished()) {
            if (isHumanTurn) {
                this.makeHumanMove(scanner);
                isHumanTurn = false;
            } else {
                this.makeComputerMove();
                isHumanTurn = true;
            }

            this.drawBoardToConsole();
        }

        System.out.print("Spielende!");
        scanner.close();

        this.printGameResult();
    }

    private void drawBoardToConsole() {
        System.out.println("\n   1  2  3");

        for (int i = 0; i < BOARD_WIDTH; i++) {
            System.out.print((i + 1) + "  ");

            for (int j = 0; j < BOARD_WIDTH; j++) {
                this.drawMarkToConsole(this.board[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    private void drawMarkToConsole(Mark mark) {
        if (mark == Mark.O) {
            System.out.print("O  ");
        } else if (mark == Mark.X) {
            System.out.print("X  ");
        } else {
            System.out.print("-  ");
        }
    }

    private boolean isGameFinished() {
        return (evaluateGameState() != Mark.EMPTY) || isBoardFull();
    }

    private boolean isBoardFull() {
        for (Mark[] row : this.board) {
            for (Mark mark : row) {
                if (mark == Mark.EMPTY) {
                    return false;
                }
            }
        }

        return true;
    }

    private void makeHumanMove(Scanner scanner) {
        System.out.println("Du bist dran! Gib zuerst die Zeile dann die Spalte ein: ");

        while (true) {
            try {
                int row = scanner.nextInt() - 1;
                int column = scanner.nextInt() - 1;

                if (this.isValidMove(row, column)) {
                    this.board[row][column] = Mark.X;
                    break;
                } else {
                    System.out.println("Ungültiger Zug, versuche es erneut.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Eingabefehler, versuche es erneut.");

                // Consume invalid input to prevent loop.
                scanner.nextLine();
            }
        }
    }

    private void makeComputerMove() {
        Random random = new Random();
        int row, column;

        do {
            row = random.nextInt(BOARD_WIDTH);
            column = random.nextInt(BOARD_WIDTH);
        } while (!isValidMove(row, column));

        this.board[row][column] = Mark.O;
        System.out.println("Der Computer hat gespielt.");
    }

    private boolean isValidMove(int row, int column) {
        return row >= 0 && row < BOARD_WIDTH
                && column >= 0 && column < BOARD_WIDTH
                && this.board[row][column] == Mark.EMPTY;
    }

    private void printGameResult() {
        Mark gameState = evaluateGameState();

        if (gameState == Mark.EMPTY) {
            System.out.println("\nUnentschieden");
        } else if (gameState == Mark.X) {
            System.out.println("\nDu gewinnst!");
        } else {
            System.out.println("\nComputer gewinnt");
        }

        this.drawBoardToConsole();
    }

    /**
     * Evaluates the current play state and returns the winner of
     * the game if there is one.
     *
     * @return the symbol of the player, who has won, or {@link Mark#EMPTY} if
     * the game is (currently) a draw.
     */
    private Mark evaluateGameState() {
        // Check rows and columns.
        for (int i = 0; i < BOARD_WIDTH; i++) {
            Mark winner = this.determineWinner(
                    board[i][0].value + board[i][1].value + board[i][2].value,
                    board[0][i].value + board[1][i].value + board[2][i].value
            );

            if (winner != Mark.EMPTY) {
                return winner;
            }
        }

        // Check diagonals.
        return this.determineWinner(
                board[0][0].value + board[1][1].value + board[2][2].value,
                board[0][2].value + board[1][1].value + board[2][0].value
        );
    }

    private Mark determineWinner(int firstSum, int secondSum) {
        if ((firstSum == -BOARD_WIDTH) || (secondSum == -BOARD_WIDTH)) {
            return Mark.O;
        } else if ((firstSum == BOARD_WIDTH) || (secondSum == BOARD_WIDTH)) {
            return Mark.X;
        }

        // No winner (yet).
        return Mark.EMPTY;
    }
}
