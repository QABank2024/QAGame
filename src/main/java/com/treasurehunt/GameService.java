// src/main/java/com/treasurehunt/GameService.java
package com.treasurehunt;

import com.treasurehunt.monster.MonsterManager;
import java.util.Random;

public class GameService {
    private Grid grid;
    private MonsterManager monsterManager;
    private boolean gameOver;
    private boolean gameWon;
    private final Random random = new Random();

    public void initializeGame(int width, int height, int monsterCount) {
        grid = new Grid(width, height);
        monsterManager = new MonsterManager(grid);
        gameOver = false;
        gameWon = false;

        placePlayerRandomly();
        placeTreasureRandomly();
        monsterManager.initializeMonsters(monsterCount);
    }

    public boolean movePlayer(Direction direction) {
        // Don't allow movement if game is over
        if (gameOver) {
            return false;
        }

        try {
            // Calculate new position
            Position currentPos = grid.getPlayerPosition();
            Position newPos = new Position(
                    currentPos.getX() + direction.getDx(),
                    currentPos.getY() + direction.getDy()
            );

            // Validate new position
            if (!grid.isValidPosition(newPos)) {
                return false;
            }

            // Check for monster at target position
            if (monsterManager.isMonsterAtPosition(newPos)) {
                grid.setPlayerPosition(newPos);
                gameOver = true;
                gameWon = false;
                return true;
            }

            // Make the move
            grid.setPlayerPosition(newPos);

            // Check for treasure collection
            if (newPos.equals(grid.getTreasurePosition())) {
                gameOver = true;
                gameWon = true;
                return true;
            }

            // Update monsters only if game isn't over
            if (!gameOver && monsterManager.getMonsters().size() > 0) {
                Position playerPos = grid.getPlayerPosition();
                monsterManager.updateMonsters();

                // Check if a monster moved onto player
                if (monsterManager.isMonsterAtPosition(playerPos)) {
                    gameOver = true;
                    gameWon = false;
                    return true;
                }
            }

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    void checkGameConditions() {
        if (grid.getPlayerPosition().equals(grid.getTreasurePosition())) {
            gameWon = true;
            gameOver = true;
        } else if (monsterManager.isMonsterAtPosition(grid.getPlayerPosition())) {
            gameWon = false;  // Explicitly set to false when caught by monster
            gameOver = true;
        }
    }

    public GameStatus getGameStatus() {
        String message;
        if (gameWon) {
            message = "Congratulations! You found the treasure!";
        } else if (gameOver) {
            message = "Game Over! A monster caught you!";
        } else {
            message = String.format("You are %d steps away from the treasure.", getDistanceToTreasure());
        }

        return new GameStatus(getDistanceToTreasure(), gameOver, gameWon, message);
    }

    private void placePlayerRandomly() {
        Position pos;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            pos = new Position(
                    random.nextInt(grid.getWidth()),
                    random.nextInt(grid.getHeight())
            );
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                // If we can't find a valid position after many attempts,
                // place in the first valid position we can find
                for (int x = 0; x < grid.getWidth(); x++) {
                    for (int y = 0; y < grid.getHeight(); y++) {
                        pos = new Position(x, y);
                        if (isValidInitialPosition(pos)) {
                            grid.setPlayerPosition(pos);
                            return;
                        }
                    }
                }
                throw new IllegalStateException("Unable to place player in valid position");
            }
        } while (!isValidInitialPosition(pos));

        grid.setPlayerPosition(pos);
    }

    private void placeTreasureRandomly() {
        Position pos;
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        do {
            pos = new Position(
                    random.nextInt(grid.getWidth()),
                    random.nextInt(grid.getHeight())
            );
            attempts++;
            if (attempts >= MAX_ATTEMPTS) {
                // If we can't find a valid position after many attempts,
                // place in the first valid position we can find
                for (int x = 0; x < grid.getWidth(); x++) {
                    for (int y = 0; y < grid.getHeight(); y++) {
                        pos = new Position(x, y);
                        if (isValidInitialPosition(pos) && !pos.equals(grid.getPlayerPosition())) {
                            grid.setTreasurePosition(pos);
                            return;
                        }
                    }
                }
                throw new IllegalStateException("Unable to place treasure in valid position");
            }
        } while (!isValidInitialPosition(pos) || pos.equals(grid.getPlayerPosition()));

        grid.setTreasurePosition(pos);
    }

    private boolean isValidInitialPosition(Position pos) {
        return grid.isValidPosition(pos) &&
                (monsterManager == null || !monsterManager.isMonsterAtPosition(pos));
    }

    public int getDistanceToTreasure() {
        return grid.getPlayerPosition().calculateDistance(grid.getTreasurePosition());
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public Grid getGrid() {
        return grid;
    }

    public MonsterManager getMonsterManager() {
        return monsterManager;
    }
}