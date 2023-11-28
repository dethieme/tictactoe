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

        while (true) {
            board.drawBoard();
            System.out.println("Mache deinen Zug. Gib zuerst x dann y ein: ");

            int xCoordinate;
            int yCoordinate;

            try {
                xCoordinate = Integer.parseInt(consoleInputReader.readLine());
                yCoordinate = Integer.parseInt(consoleInputReader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Eingabefehler, versuche es erneut.");
                continue;
            }

            if (!board.attemptHumanPlayerMove(yCoordinate, xCoordinate)) {
                System.out.println("Ungültiger Zug, bitte noch einmal...");
                continue;
            }

            if (board.isGameFinished()) {
                wrapUpGame(board);
                break;
            }

            board.compZug();
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