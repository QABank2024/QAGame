// src/test/java/com/treasurehunt/monster/MonsterManagerTest.java
package com.treasurehunt.monster;

import com.treasurehunt.Grid;
import com.treasurehunt.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

class MonsterManagerTest {
    private MonsterManager monsterManager;
    private Grid grid;
    private static final int GRID_SIZE = 5;
    private static final int MONSTER_COUNT = 10; // Increased count to ensure mix of types

    @BeforeEach
    void setUp() {
        grid = new Grid(GRID_SIZE, GRID_SIZE);
        grid.setPlayerPosition(new Position(0, 0));
        grid.setTreasurePosition(new Position(4, 4));
        monsterManager = new MonsterManager(grid);
    }

    @Test
    void shouldPlaceCorrectNumberOfMonsters() {
        monsterManager.initializeMonsters(MONSTER_COUNT);
        assertEquals(MONSTER_COUNT, monsterManager.getMonsters().size());
    }

    @RepeatedTest(10)
    void shouldPlaceMonstersInValidPositions() {
        monsterManager.initializeMonsters(MONSTER_COUNT);
        for (Monster monster : monsterManager.getMonsters()) {
            Position pos = monster.getPosition();
            assertTrue(pos.getX() >= 0 && pos.getX() < GRID_SIZE);
            assertTrue(pos.getY() >= 0 && pos.getY() < GRID_SIZE);
            assertFalse(pos.equals(grid.getPlayerPosition()));
            assertFalse(pos.equals(grid.getTreasurePosition()));
        }
    }

    @Test
    void shouldCreateDifferentTypesOfMonsters() {
        // Given - Initialize with enough monsters to ensure mix of types
        monsterManager.initializeMonsters(MONSTER_COUNT);

        // When/Then - Check for presence of both types
        boolean hasAggressive = false;
        boolean hasPassive = false;
        int attempts = 0;

        while ((!hasAggressive || !hasPassive) && attempts < 5) {
            monsterManager.initializeMonsters(MONSTER_COUNT);
            for (Monster monster : monsterManager.getMonsters()) {
                if (monster instanceof AggressiveMonster) hasAggressive = true;
                if (monster instanceof PassiveMonster) hasPassive = true;

                if (hasAggressive && hasPassive) break;
            }
            attempts++;
        }

        String errorMessage = String.format(
                "Should have both types of monsters after %d attempts with %d monsters. " +
                        "Has Aggressive: %b, Has Passive: %b",
                attempts, MONSTER_COUNT, hasAggressive, hasPassive
        );
        assertTrue(hasAggressive && hasPassive, errorMessage);
    }

    @Test
    void shouldDetectMonsterCollision() {
        monsterManager.initializeMonsters(1);
        Monster monster = monsterManager.getMonsters().get(0);
        Position monsterPos = monster.getPosition();

        assertTrue(monsterManager.isMonsterAtPosition(monsterPos));
        assertFalse(monsterManager.isMonsterAtPosition(new Position(0, 0)));
    }
}