// src/main/java/com/treasurehunt/Grid.java
package com.treasurehunt;

public class Grid {
    private final int width;
    private final int height;
    private Position playerPosition;
    private Position treasurePosition;

    public Grid(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Grid dimensions must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Position position) {
        validatePosition(position);
        this.playerPosition = position;
    }

    public Position getTreasurePosition() {
        return treasurePosition;
    }

    public void setTreasurePosition(Position position) {
        validatePosition(position);
        this.treasurePosition = position;
    }

    // Changed from private to public
    public void validatePosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Position cannot be null");
        }
        if (position.getX() < 0 || position.getX() >= width ||
                position.getY() < 0 || position.getY() >= height) {
            throw new IllegalArgumentException(
                    String.format("Position (%d, %d) is outside the grid bounds of %dx%d",
                            position.getX(), position.getY(), width, height)
            );
        }
    }

    public boolean isValidPosition(Position position) {
        try {
            validatePosition(position);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}