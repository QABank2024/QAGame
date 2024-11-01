// src/test/java/com/treasurehunt/GameControllerTest.java
package com.treasurehunt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    private GameController controller;
    private static final int DEFAULT_SIZE = 5;
    private static final int DEFAULT_MONSTERS = 0; // No monsters for movement tests

    @BeforeEach
    void setUp() {
        controller = new GameController();
        assertTrue(controller.initializeGame(DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_MONSTERS));
    }

    @Test
    void shouldProcessAllValidDirections() {
        // Initialize new game with known player position in the middle
        controller.initializeGame(DEFAULT_SIZE, DEFAULT_SIZE, 0); // No monsters for this test
        GameService gameService = controller.getGameService();

        // Place player in center and treasure far from test movement area
        Position centerPosition = new Position(2, 2);
        gameService.getGrid().setPlayerPosition(centerPosition);
        gameService.getGrid().setTreasurePosition(new Position(4, 4)); // Place treasure away from test area

        // Verify initial state
        assertFalse(gameService.isGameOver(), "Game should not be over at start");

        // Test UP
        assertTrue(controller.processCommand("move up"), "Should accept move up");
        assertEquals(1, gameService.getGrid().getPlayerPosition().getY(), "Should move up one position");
        assertFalse(gameService.isGameOver(), "Game should not be over after moving up");
        gameService.getGrid().setPlayerPosition(centerPosition); // Reset position

        // Test DOWN
        assertTrue(controller.processCommand("move down"), "Should accept move down");
        assertEquals(3, gameService.getGrid().getPlayerPosition().getY(), "Should move down one position");
        assertFalse(gameService.isGameOver(), "Game should not be over after moving down");
        gameService.getGrid().setPlayerPosition(centerPosition); // Reset position

        // Test LEFT
        assertTrue(controller.processCommand("move left"), "Should accept move left");
        assertEquals(1, gameService.getGrid().getPlayerPosition().getX(), "Should move left one position");
        assertFalse(gameService.isGameOver(), "Game should not be over after moving left");
        gameService.getGrid().setPlayerPosition(centerPosition); // Reset position

        // Test RIGHT
        assertTrue(controller.processCommand("move right"), "Should accept move right");
        assertEquals(3, gameService.getGrid().getPlayerPosition().getX(), "Should move right one position");

        // Verify final state
        assertFalse(gameService.isGameOver(), "Game should not be over after valid moves");
        assertTrue(gameService.getGrid().isValidPosition(gameService.getGrid().getPlayerPosition()),
                "Final position should be valid");
    }

    @Test
    void shouldRejectInvalidCommands() {
        // Test null command
        assertFalse(controller.processCommand(null), "Should reject null command");

        // Test empty command
        assertFalse(controller.processCommand(""), "Should reject empty command");

        // Test invalid direction
        assertFalse(controller.processCommand("move diagonal"), "Should reject invalid direction");

        // Test invalid command format
        assertFalse(controller.processCommand("run up"), "Should reject invalid command format");
    }

    @Test
    void shouldHandleUninitializedGame() {
        GameController uninitializedController = new GameController();
        assertFalse(uninitializedController.processCommand("move up"),
                "Should reject commands when game is not initialized");

        assertThrows(IllegalStateException.class,
                () -> uninitializedController.getGameStatus(),
                "Should throw exception when getting status of uninitialized game");
    }
}