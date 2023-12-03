/**
 * The main class of the application.
 */
public class Main {
    public static void main(final String[] args) {
        System.out.println("Herzlich Willkommen. Dein Symbol ist das X.");

        Board board = new Board();
        board.startGameLoop();
    }
}