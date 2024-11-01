// src/test/java/com/treasurehunt/GameServiceTest.java
package com.treasurehunt;

import com.treasurehunt.monster.Monster;
import com.treasurehunt.monster.MonsterManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private GameService gameService;
    private static final int GRID_SIZE = 5;
    private static final int MONSTER_COUNT = 2;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
        gameService.initializeGame(GRID_SIZE, GRID_SIZE, MONSTER_COUNT);
    }

    @Test
    void shouldMovePlayerInValidDirection() {
        // Place player in a position where movement is definitely possible
        Grid grid = gameService.getGrid();
        Position initialPos = new Position(2, 2);
        grid.setPlayerPosition(initialPos);

        boolean moved = gameService.movePlayer(Direction.RIGHT);

        assertTrue(moved);
        Position newPos = grid.getPlayerPosition();
        assertEquals(initialPos.getX() + 1, newPos.getX());
        assertEquals(initialPos.getY(), newPos.getY());
    }

    @Test
    void shouldDetectMonsterCollision() {
        // Create a specific scenario for monster collision
        Grid grid = gameService.getGrid();
        Position playerPos = new Position(2, 2);
        grid.setPlayerPosition(playerPos);

        // Force a monster to the player's position
        Monster monster = gameService.getMonsterManager().getMonsters().get(0);
        monster.setPosition(playerPos);

        // Check game conditions
        gameService.checkGameConditions();

        assertTrue(gameService.isGameOver());
        assertFalse(gameService.isGameWon());
    }

    @Test
    void shouldInitializeGameWithValidGrid() {
        assertNotNull(gameService.getGrid());
        assertEquals(GRID_SIZE, gameService.getGrid().getWidth());
        assertEquals(GRID_SIZE, gameService.getGrid().getHeight());
    }

    @RepeatedTest(50)
    void shouldPlacePlayerRandomlyWithinGrid() {
        Position playerPos = gameService.getGrid().getPlayerPosition();
        assertNotNull(playerPos);
        assertTrue(playerPos.getX() >= 0 && playerPos.getX() < GRID_SIZE);
        assertTrue(playerPos.getY() >= 0 && playerPos.getY() < GRID_SIZE);
    }

    @RepeatedTest(50)
    void shouldPlaceTreasureRandomlyWithinGrid() {
        Position treasurePos = gameService.getGrid().getTreasurePosition();
        assertNotNull(treasurePos);
        assertTrue(treasurePos.getX() >= 0 && treasurePos.getX() < GRID_SIZE);
        assertTrue(treasurePos.getY() >= 0 && treasurePos.getY() < GRID_SIZE);
    }

    @Test
    void shouldCalculateCorrectDistanceToTreasure() {
        Grid grid = gameService.getGrid();
        grid.setPlayerPosition(new Position(0, 0));
        grid.setTreasurePosition(new Position(3, 4));

        assertEquals(5, gameService.getDistanceToTreasure());
    }

    @Test
    void shouldPreventMovingOutsideGrid() {
        Grid grid = gameService.getGrid();
        grid.setPlayerPosition(new Position(0, 0));

        assertFalse(gameService.movePlayer(Direction.LEFT));
        assertFalse(gameService.movePlayer(Direction.UP));
    }

    @Test
    void shouldInitializeWithCorrectNumberOfMonsters() {
        assertEquals(MONSTER_COUNT, gameService.getMonsterManager().getMonsters().size());
    }
}