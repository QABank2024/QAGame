// src/main/java/com/treasurehunt/GameController.java
package com.treasurehunt;

public class GameController {
    private GameService gameService;
    private static final int MAX_MONSTERS_RATIO = 4; // Maximum 1 monster per 4 grid cells

    public boolean initializeGame(int width, int height, int monsterCount) {
        if (width <= 0 || height <= 0) return false;
        if (monsterCount > (width * height) / MAX_MONSTERS_RATIO) return false;

        try {
            gameService = new GameService();
            gameService.initializeGame(width, height, monsterCount);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean processCommand(String command) {
        if (gameService == null || command == null) {
            return false;
        }

        String[] parts = command.trim().toLowerCase().split("\\s+");
        if (parts.length != 2 || !parts[0].equals("move")) {
            return false;
        }

        Direction direction;
        try {
            direction = Direction.valueOf(parts[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }

        return gameService.movePlayer(direction);
    }

    public GameStatus getGameStatus() {
        if (gameService == null) {
            throw new IllegalStateException("Game has not been initialized");
        }
        return gameService.getGameStatus();
    }

    public GameService getGameService() {
        return gameService;
    }
}