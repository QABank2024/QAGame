// Game.java

import java.util.Random;
import java.util.Scanner;

public class Game {
    private int gridSize;
    private int playerX, playerY;
    private int treasureX, treasureY;
    private Monster[] monsters;

    public Game(int gridSize, int numMonsters) {
        this.gridSize = gridSize;
        initializePlayerPosition();
        placeTreasure();
        initializeMonsters(numMonsters);
    }

    private void initializePlayerPosition() {
        Random random = new Random();
        playerX = random.nextInt(gridSize);
        playerY = random.nextInt(gridSize);
    }

    private void placeTreasure() {
        Random random = new Random();
        treasureX = random.nextInt(gridSize);
        treasureY = random.nextInt(gridSize);
    }

    private void initializeMonsters(int numMonsters) {
        monsters = new Monster[numMonsters];
        Random random = new Random();
        for (int i = 0; i < numMonsters; i++) {
            int monsterX = random.nextInt(gridSize);
            int monsterY = random.nextInt(gridSize);
            monsters[i] = new Monster(monsterX, monsterY, "Monster" + (i + 1));
        }
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
                if (checkLoss()) {
                    System.out.println("Oh no! A monster caught you. Game over.");
                    break;
                }
                giveDistanceHint();
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

        // Check for collision with any monster after moving
        if (checkLoss()) {
            System.out.println("Oh no! A monster caught you. Game over.");
            System.exit(0); // End the game if player lands on a monster
        }

        return true;
    }

    private boolean checkWin() {
        return playerX == treasureX && playerY == treasureY;
    }

    private boolean checkLoss() {
        for (Monster monster : monsters) {
            if (playerX == monster.getX() && playerY == monster.getY()) {
                monster.greet();
                return true;
            }
        }
        return false;
    }

    private void giveDistanceHint() {
        int distance = Math.abs(playerX - treasureX) + Math.abs(playerY - treasureY);
        System.out.println("You are " + distance + " moves away from the treasure.");
    }
}
