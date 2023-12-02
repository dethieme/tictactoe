import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The main class of the application.
 */
public class Main {

    public static void main(final String[] args) {
        Board board = new Board();

        InputStreamReader streamReader = new InputStreamReader(System.in);
        BufferedReader consoleInputReader = new BufferedReader(streamReader);
        System.out.println("Herzlich Willkommen. Dein Symbol ist das X.");

        while (!board.isGameFinished()) {
            board.drawBoard();
            System.out.println("Mache deinen Zug. Gib zuerst die Zeile dann die Spalte ein: ");

            int row;
            int column;

            try {
                row = Integer.parseInt(consoleInputReader.readLine());
                column = Integer.parseInt(consoleInputReader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Eingabefehler, versuche es erneut.");
                continue;
            }

            if (!board.makeHumanMove(row, column)) {
                System.out.println("Ungültiger Zug, versuche es erneut...");
                continue;
            }

            if (board.isGameFinished()) {
                wrapUpGame(board);
                break;
            }

            board.makeComputerMove();
            System.out.println("Der Computer hat gespielt: ");

            if (board.isGameFinished()) {
                wrapUpGame(board);
                break;
            }
        }
    }

    private static void wrapUpGame(final Board board) {
        System.out.print("Spielende! ");
        board.printGameResult();
        board.drawBoard();
    }
}