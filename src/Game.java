import java.util.Random;
import java.util.Scanner;

public class Game {
    private int gridSize;
    private int playerX, playerY;
    private int treasureX, treasureY;
    private Monster[] monsters;
    private Grid grid;
    private int numObstacles;

    public Game(int gridSize, int numMonsters, int numObstacles) {
        if (gridSize < 2) {
            throw new IllegalArgumentException("Grid size must be at least 2.");
        }
        if (numMonsters + numObstacles >= gridSize * gridSize - 2) {
            throw new IllegalArgumentException("Too many monsters and obstacles for the grid size.");
        }

        this.gridSize = gridSize;
        this.numObstacles = numObstacles;
        this.monsters = new Monster[numMonsters]; // Initialize monsters array
        grid = new Grid(gridSize);

        initializePlayerPosition();
        placeTreasure();
        initializeMonsters(numMonsters);
        initializeObstacles();
        updateGrid();
    }

    private void initializePlayerPosition() {
        Random random = new Random();
        do {
            playerX = random.nextInt(gridSize);
            playerY = random.nextInt(gridSize);
        } while (!isValidPlayerPosition(playerX, playerY));
    }

    private boolean isValidPlayerPosition(int x, int y) {
        // Check distance from the treasure
        if (Math.abs(x - treasureX) <= 2 && Math.abs(y - treasureY) <= 2) {
            return false; // Too close to the treasure
        }

        // Check distance from each monster
        for (Monster monster : monsters) {
            if (monster != null && Math.abs(x - monster.getX()) <= 2 && Math.abs(y - monster.getY()) <= 2) {
                return false; // Too close to a monster
            }
        }

        return true; // Position is valid
    }

    private void placeTreasure() {
        Random random = new Random();
        do {
            treasureX = random.nextInt(gridSize);
            treasureY = random.nextInt(gridSize);
        } while (!isValidTreasurePosition(treasureX, treasureY));
    }

    private boolean isValidTreasurePosition(int x, int y) {
        // Check if the position overlaps with player or any monster
        if ((x == playerX && y == playerY) || isMonsterAtPosition(x, y)) {
            return false;
        }
        return true;
    }

    private void initializeMonsters(int numMonsters) {
        Random random = new Random();
        for (int i = 0; i < numMonsters; i++) {
            int monsterX, monsterY;
            do {
                monsterX = random.nextInt(gridSize);
                monsterY = random.nextInt(gridSize);
            } while (!isValidMonsterPosition(monsterX, monsterY));
            monsters[i] = new Monster(monsterX, monsterY, "Monster" + (i + 1));
        }
    }

    private boolean isValidMonsterPosition(int x, int y) {
        // Check if the position overlaps with player, treasure, or other monsters
        if ((x == playerX && y == playerY) || (x == treasureX && y == treasureY)) {
            return false;
        }
        for (Monster monster : monsters) {
            if (monster != null && monster.getX() == x && monster.getY() == y) {
                return false; // Overlaps with another monster
            }
        }
        return true; // Position is valid
    }

    private void initializeObstacles() {
        Random random = new Random();
        int count = 0;
        int totalObstacleCells = 0; // Track total cells occupied by obstacles

        while (count < numObstacles) {
            int length = 1; // Set obstacle length to 1 for all obstacles
            int startX = random.nextInt(gridSize);
            int startY = random.nextInt(gridSize);

            // Check if the obstacle can fit on the grid without overlapping existing obstacles, the treasure, or monsters
            if (!grid.isObstacle(startX, startY) &&
                    !(startX == treasureX && startY == treasureY) &&
                    !isMonsterAtPosition(startX, startY)) {
                grid.placeObstacle(startX, startY);
                count++;
                totalObstacleCells++; // Increase the count of occupied cells
            }

            // Check if total occupied cells exceed maximum allowed
            if (totalObstacleCells > (gridSize * gridSize - 2)) { // -2 for player and treasure
                System.out.println("Exceeded maximum obstacle cells. Adjusting.");
                break; // Stop adding obstacles if limit exceeded
            }
        }
    }

    private void updateGrid() {
        grid.clearPosition(playerX, playerY); // Clear previous player position
        grid.updatePosition(playerX, playerY, 'X'); // Update player position
        grid.updatePosition(treasureX, treasureY, 'T'); // Update treasure position

        // Update monster positions
        for (Monster monster : monsters) {
            if (monster != null) {
                grid.updatePosition(monster.getX(), monster.getY(), 'M'); // Monsters
            }
        }

        grid.displayGrid(); // Show updated grid
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Treasure Hunt Game!");

        while (true) {
            System.out.println("Enter your move (WASD): ");
            String move = scanner.nextLine().toUpperCase();

            if (movePlayer(move)) {
                if (checkWin()) {
                    System.out.println("Congratulations! You found the treasure!");
                    break;
                }
                moveMonsters(); // Move monsters after player move

                // Check if a monster caught the player after they moved
                if (checkLoss()) {
                    System.out.println("Oh no! A monster caught you. Game over.");
                    break;
                }

                updateGrid(); // Update grid after all moves
            } else {
                System.out.println("Invalid move. Try again.");
            }
        }
        scanner.close();
    }

    private boolean movePlayer(String direction) {
        switch (direction) {
            case "W": if (playerY > 0) playerY--; else return false; break;
            case "S": if (playerY < gridSize - 1) playerY++; else return false; break;
            case "A": if (playerX > 0) playerX--; else return false; break;
            case "D": if (playerX < gridSize - 1) playerX++; else return false; break;
            default: return false;
        }

        return true; // Player successfully moved
    }

    private void moveMonsters() {
        for (Monster monster : monsters) {
            if (monster != null) {
                // Simple movement logic towards player
                int monsterX = monster.getX();
                int monsterY = monster.getY();

                if (monsterX < playerX) {
                    if (!grid.isObstacle(monsterX + 1, monsterY)) {
                        monster.moveRight();
                    }
                } else if (monsterX > playerX) {
                    if (!grid.isObstacle(monsterX - 1, monsterY)) {
                        monster.moveLeft();
                    }
                }

                if (monsterY < playerY) {
                    if (!grid.isObstacle(monsterX, monsterY + 1)) {
                        monster.moveDown();
                    }
                } else if (monsterY > playerY) {
                    if (!grid.isObstacle(monsterX, monsterY - 1)) {
                        monster.moveUp();
                    }
                }
            }
        }
    }

    private boolean checkWin() {
        return playerX == treasureX && playerY == treasureY;
    }

    private boolean checkLoss() {
        for (Monster monster : monsters) {
            if (monster != null && playerX == monster.getX() && playerY == monster.getY()) {
                monster.greet();
                return true;
            }
        }
        return false;
    }

    private boolean isMonsterAtPosition(int x, int y) {
        for (Monster monster : monsters) {
            if (monster != null && monster.getX() == x && monster.getY() == y) {
                return true; // Monster is at this position
            }
        }
        return false;
    }
}
