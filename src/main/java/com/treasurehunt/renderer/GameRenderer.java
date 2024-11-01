// src/main/java/com/treasurehunt/renderer/GameRenderer.java
package com.treasurehunt.renderer;

import com.treasurehunt.Grid;
import com.treasurehunt.Position;
import com.treasurehunt.monster.Monster;
import com.treasurehunt.monster.MonsterManager;
import com.treasurehunt.monster.AggressiveMonster;

import java.util.List;

public class GameRenderer {
    private static final char EMPTY_CELL = '·';
    private static final char PLAYER = 'P';
    private static final char TREASURE = 'T';
    private static final char AGGRESSIVE_MONSTER = 'A';
    private static final char PASSIVE_MONSTER = 'M';
    private static final String HORIZONTAL_LINE = "───";
    private static final String VERTICAL_LINE = "│";

    private static final String TOP_LEFT = "┌";
    private static final String TOP_RIGHT = "┐";
    private static final String BOTTOM_LEFT = "└";
    private static final String BOTTOM_RIGHT = "┘";
    private static final String TOP_JOINT = "┬";
    private static final String BOTTOM_JOINT = "┴";
    private static final String LEFT_JOINT = "├";
    private static final String RIGHT_JOINT = "┤";
    private static final String CROSS = "┼";

    public String render(Grid grid, MonsterManager monsterManager) {
        StringBuilder display = new StringBuilder();
        char[][] gameBoard = createGameBoard(grid, monsterManager);

        // Build the display string with borders
        // Top border
        display.append(createTopBorder(grid.getWidth())).append("\n");

        // Grid rows
        for (int y = 0; y < grid.getHeight(); y++) {
            // Cell contents with vertical borders
            display.append(VERTICAL_LINE);
            for (int x = 0; x < grid.getWidth(); x++) {
                display.append(" ").append(gameBoard[y][x]).append(" ").append(VERTICAL_LINE);
            }
            display.append("\n");

            // Add horizontal line between rows (except after last row)
            if (y < grid.getHeight() - 1) {
                display.append(createMiddleBorder(grid.getWidth())).append("\n");
            }
        }

        // Bottom border
        display.append(createBottomBorder(grid.getWidth())).append("\n");

        // Add legend
        display.append("\nLegend:\n");
        display.append(PLAYER).append(" - Player\n");
        display.append(TREASURE).append(" - Treasure\n");
        display.append(AGGRESSIVE_MONSTER).append(" - Aggressive Monster\n");
        display.append(PASSIVE_MONSTER).append(" - Passive Monster\n");
        display.append(EMPTY_CELL).append(" - Empty Space\n");

        return display.toString();
    }

    private char[][] createGameBoard(Grid grid, MonsterManager monsterManager) {
        char[][] board = initializeBoard(grid);

        // Render entities in specific order: treasure first, then player, then monsters last
        // This ensures proper overlay priority

        // 1. Place treasure (lowest priority)
        if (grid.getTreasurePosition() != null) {
            placeEntity(board, grid.getTreasurePosition(), TREASURE);
        }

        // 2. Place player (medium priority)
        if (grid.getPlayerPosition() != null) {
            placeEntity(board, grid.getPlayerPosition(), PLAYER);
        }

        // 3. Place monsters (highest priority)
        for (Monster monster : monsterManager.getMonsters()) {
            char symbol = (monster instanceof AggressiveMonster) ? AGGRESSIVE_MONSTER : PASSIVE_MONSTER;
            placeEntity(board, monster.getPosition(), symbol);
        }

        return board;
    }

    private char[][] initializeBoard(Grid grid) {
        char[][] board = new char[grid.getHeight()][grid.getWidth()];
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                board[y][x] = EMPTY_CELL;
            }
        }
        return board;
    }

    private void placeEntity(char[][] board, Position position, char symbol) {
        if (position != null) {
            board[position.getY()][position.getX()] = symbol;
        }
    }

    private String createTopBorder(int width) {
        StringBuilder border = new StringBuilder(TOP_LEFT);
        for (int i = 0; i < width - 1; i++) {
            border.append(HORIZONTAL_LINE).append(TOP_JOINT);
        }
        border.append(HORIZONTAL_LINE).append(TOP_RIGHT);
        return border.toString();
    }

    private String createMiddleBorder(int width) {
        StringBuilder border = new StringBuilder(LEFT_JOINT);
        for (int i = 0; i < width - 1; i++) {
            border.append(HORIZONTAL_LINE).append(CROSS);
        }
        border.append(HORIZONTAL_LINE).append(RIGHT_JOINT);
        return border.toString();
    }

    private String createBottomBorder(int width) {
        StringBuilder border = new StringBuilder(BOTTOM_LEFT);
        for (int i = 0; i < width - 1; i++) {
            border.append(HORIZONTAL_LINE).append(BOTTOM_JOINT);
        }
        border.append(HORIZONTAL_LINE).append(BOTTOM_RIGHT);
        return border.toString();
    }
}