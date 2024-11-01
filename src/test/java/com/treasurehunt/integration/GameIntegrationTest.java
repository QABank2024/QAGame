// src/test/java/com/treasurehunt/integration/GameIntegrationTest.java
package com.treasurehunt.integration;

import com.treasurehunt.*;
import com.treasurehunt.monster.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameIntegrationTest {
    private GameService gameService;
    private static final int GRID_SIZE = 5;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        gameService.initializeGame(GRID_SIZE, GRID_SIZE, 0);
    }

    @Test
    void shouldMaintainValidGameStateAfterMultipleMoves() {
        // Given - Setup initial game state
        Grid grid = gameService.getGrid();
        gameService.getMonsterManager().initializeMonsters(0); // Ensure no monsters exist

        // Place player in center
        Position startPos = new Position(2, 2);
        grid.setPlayerPosition(startPos);

        // Place treasure far from movement path
        Position treasurePos = new Position(0, 0);
        grid.setTreasurePosition(treasurePos);

        // Verify initial state
        assertFalse(gameService.isGameOver(), "Game should not be over at start");
        assertTrue(grid.isValidPosition(startPos), "Start position should be valid");
        assertNotEquals(treasurePos, startPos, "Player should not start at treasure");

        // When - Move UP
        boolean movedUp = gameService.movePlayer(Direction.UP);

        // Then - Verify state after UP
        assertTrue(movedUp, "Should move up successfully");
        assertFalse(gameService.isGameOver(), "Game should not be over after up");
        assertEquals(1, grid.getPlayerPosition().getY(), "Should be at y=1");

        // Reset position
        grid.setPlayerPosition(startPos);

        // When - Move RIGHT
        boolean movedRight = gameService.movePlayer(Direction.RIGHT);

        // Then - Verify state after RIGHT
        assertTrue(movedRight, "Should move right successfully");
        assertFalse(gameService.isGameOver(), "Game should not be over after right");
        assertEquals(3, grid.getPlayerPosition().getX(), "Should be at x=3");

        // Then - Verify final state
        assertFalse(gameService.isGameOver(), "Game should not be over at end");
        assertFalse(gameService.isGameWon(), "Game should not be won");
        assertTrue(grid.isValidPosition(grid.getPlayerPosition()), "Final position should be valid");
        assertNotEquals(grid.getPlayerPosition(), treasurePos, "Should not be at treasure position");
    }
}