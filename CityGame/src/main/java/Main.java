import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Game game = new Game(scanner);
        game.startGame(game.scanner);
    }
}
