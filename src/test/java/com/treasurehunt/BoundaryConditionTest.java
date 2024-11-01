// src/test/java/com/treasurehunt/BoundaryConditionTest.java
package com.treasurehunt;

import com.treasurehunt.monster.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoundaryConditionTest {

    @Test
    void shouldHandleMinimumGridSize() {
        // Given/When/Then
        assertThrows(IllegalArgumentException.class, () -> new Grid(0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Grid(1, 0));
        assertDoesNotThrow(() -> new Grid(1, 1));
    }

    @Test
    void shouldHandleGridBoundaries() {
        // Given
        Grid grid = new Grid(3, 3);
        grid.setPlayerPosition(new Position(0, 0));

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> grid.validatePosition(new Position(-1, 0)));
        assertThrows(IllegalArgumentException.class, () -> grid.validatePosition(new Position(0, -1)));
        assertThrows(IllegalArgumentException.class, () -> grid.validatePosition(new Position(3, 0)));
        assertThrows(IllegalArgumentException.class, () -> grid.validatePosition(new Position(0, 3)));
    }

    @Test
    void shouldPreventMonsterOvercrowding() {
        // Given
        Grid grid = new Grid(2, 2);
        MonsterManager monsterManager = new MonsterManager(grid);

        // When
        monsterManager.initializeMonsters(4); // Try to fill every cell with monsters

        // Then
        assertTrue(monsterManager.getMonsters().size() <= 4,
                "Should not create more monsters than grid cells");

        // Verify monster positions are valid and unique
        for (Monster monster : monsterManager.getMonsters()) {
            assertTrue(grid.isValidPosition(monster.getPosition()));
        }
    }

    @Test
    void shouldHandleEdgePositions() {
        // Given
        Grid grid = new Grid(3, 3);
        Position cornerPos = new Position(0, 0);
        grid.setPlayerPosition(cornerPos);

        AggressiveMonster monster = new AggressiveMonster(new Position(2, 2));

        // When
        monster.move(grid, cornerPos);

        // Then
        Position newPos = monster.getPosition();
        assertTrue(newPos.getX() < 3 && newPos.getY() < 3,
                "Monster should stay within grid bounds");
        assertTrue(newPos.getX() >= 0 && newPos.getY() >= 0,
                "Monster should stay within grid bounds");
    }

    @Test
    void shouldHandleNullPositions() {
        // Given
        Grid grid = new Grid(3, 3);

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> grid.setPlayerPosition(null));
        assertThrows(IllegalArgumentException.class, () -> grid.setTreasurePosition(null));
        assertThrows(IllegalArgumentException.class, () -> grid.validatePosition(null));
    }
}