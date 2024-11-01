// src/test/java/com/treasurehunt/GameStateTransitionTest.java
package com.treasurehunt;

import com.treasurehunt.monster.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameStateTransitionTest {
    private GameService gameService;
    private static final int GRID_SIZE = 5;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        gameService.initializeGame(GRID_SIZE, GRID_SIZE, 0); // Start with no monsters
    }

    @Test
    void shouldTransitionToWinState() {
        // Given
        Grid grid = gameService.getGrid();
        Position playerPos = new Position(1, 1);
        Position treasurePos = new Position(1, 2);
        grid.setPlayerPosition(playerPos);
        grid.setTreasurePosition(treasurePos);

        // When
        gameService.movePlayer(Direction.DOWN);

        // Then
        assertTrue(gameService.isGameWon());
        assertTrue(gameService.isGameOver());
        GameStatus status = new GameStatus(0, true, true, "Congratulations! You found the treasure!");
        assertEquals(status.getMessage(), gameService.getGameStatus().getMessage());
    }

    @Test
    void shouldTransitionToLoseState() {
        // Given
        Grid grid = gameService.getGrid();
        Position playerPos = new Position(1, 1);
        grid.setPlayerPosition(playerPos);

        Monster monster = new AggressiveMonster(new Position(1, 2));
        gameService.getMonsterManager().addMonster(monster);

        // When
        gameService.movePlayer(Direction.DOWN);

        // Then
        assertTrue(gameService.isGameOver());
        assertFalse(gameService.isGameWon());
        assertTrue(gameService.getGameStatus().getMessage().contains("Game Over"));
    }

    @Test
    void shouldUpdateGameStatusMessage() {
        // Given
        Grid grid = gameService.getGrid();
        Position playerPos = new Position(0, 0);
        Position treasurePos = new Position(3, 4);
        grid.setPlayerPosition(playerPos);
        grid.setTreasurePosition(treasurePos);

        // When
        GameStatus status = gameService.getGameStatus();

        // Then
        assertEquals(5, status.getDistanceToTreasure());
        assertTrue(status.getMessage().contains("5 steps away"));
        assertFalse(status.isGameOver());
    }
}