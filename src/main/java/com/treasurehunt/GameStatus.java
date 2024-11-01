// src/main/java/com/treasurehunt/GameStatus.java
package com.treasurehunt;

public class GameStatus {
    private final int distanceToTreasure;
    private final boolean gameOver;
    private final boolean gameWon;
    private final String message;

    public GameStatus(int distanceToTreasure, boolean gameOver, boolean gameWon, String message) {
        this.distanceToTreasure = distanceToTreasure;
        this.gameOver = gameOver;
        this.gameWon = gameWon;
        this.message = message;
    }

    public int getDistanceToTreasure() {
        return distanceToTreasure;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public String getMessage() {
        return message;
    }
}