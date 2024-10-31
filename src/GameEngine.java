import java.util.Scanner;

public class GameEngine {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int gridSize;
        do {
            System.out.print("Enter grid size (minimum 2): ");
            gridSize = scanner.nextInt();
        } while (gridSize < 2);

        int numMonsters;
        int numObstacles;
        int maxEntities = (gridSize * gridSize) - 2; // One for player, one for treasure

        do {
            System.out.print("Enter number of monsters (maximum " + maxEntities + "): ");
            numMonsters = scanner.nextInt();
        } while (numMonsters < 0 || numMonsters > maxEntities);

        do {
            System.out.print("Enter number of obstacles (maximum " + (maxEntities - numMonsters) + "): ");
            numObstacles = scanner.nextInt();
        } while (numObstacles < 0 || numObstacles > (maxEntities - numMonsters));

        Game game = new Game(gridSize, numMonsters, numObstacles);
        game.startGame();
    }
}
