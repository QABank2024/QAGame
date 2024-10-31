import java.util.Scanner;

public class GameEngine {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter grid size: ");
        int gridSize = scanner.nextInt();

        System.out.print("Enter number of monsters: ");
        int numMonsters = scanner.nextInt();

        Game game = new Game(gridSize, numMonsters);
        game.startGame();
    }
}

