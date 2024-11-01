// src/test/java/com/treasurehunt/monster/MonsterInteractionTest.java
package com.treasurehunt.monster;

import com.treasurehunt.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonsterInteractionTest {
    private Grid grid;
    private MonsterManager monsterManager;
    private static final int GRID_SIZE = 5;

    @BeforeEach
    void setUp() {
        grid = new Grid(GRID_SIZE, GRID_SIZE);
        grid.setPlayerPosition(new Position(2, 2));
        monsterManager = new MonsterManager(grid);
    }

    @Test
    void aggressiveMonsterShouldChasePlayerEfficiently() {
        // Given
        Position playerPos = new Position(4, 4);
        Position monsterPos = new Position(0, 0);
        grid.setPlayerPosition(playerPos);

        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // When
        monster.move(grid, playerPos);

        // Then
        Position newPos = monster.getPosition();
        // Monster should move either right or down, both are optimal
        assertTrue(
                (newPos.equals(new Position(1, 0)) || newPos.equals(new Position(0, 1))),
                String.format("Monster should move to (1,0) or (0,1), but moved to (%d,%d)",
                        newPos.getX(), newPos.getY())
        );
    }

    @Test
    void aggressiveMonsterShouldMoveHorizontallyWhenCloserVertically() {
        // Given
        Position playerPos = new Position(4, 1);
        Position monsterPos = new Position(0, 1);
        grid.setPlayerPosition(playerPos);

        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // When
        monster.move(grid, playerPos);

        // Then
        Position newPos = monster.getPosition();
        assertEquals(new Position(1, 1), newPos,
                "Monster should move right when aligned vertically");
    }

    @Test
    void aggressiveMonsterShouldMoveVerticallyWhenCloserHorizontally() {
        // Given
        Position playerPos = new Position(1, 4);
        Position monsterPos = new Position(1, 0);
        grid.setPlayerPosition(playerPos);

        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // When
        monster.move(grid, playerPos);

        // Then
        Position newPos = monster.getPosition();
        assertEquals(new Position(1, 1), newPos,
                "Monster should move down when aligned horizontally");
    }

    @Test
    void aggressiveMonsterShouldStayInPositionWhenBlocked() {
        // Given - place monster in top-left corner
        Position playerPos = new Position(0, 0);
        Position monsterPos = new Position(0, 0);
        grid.setPlayerPosition(playerPos);

        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // When
        monster.move(grid, playerPos);

        // Then
        assertEquals(monsterPos, monster.getPosition(),
                "Monster should stay in position when at player's location");
    }

    @Test
    void passiveMonsterShouldMoveRandomlyWithinBounds() {
        // Given
        PassiveMonster monster = new PassiveMonster(new Position(2, 2));
        monsterManager.addMonster(monster);
        Position initialPos = monster.getPosition();
        boolean hasMoved = false;

        // When - Give multiple chances to move
        for (int i = 0; i < 10; i++) {
            monster.move(grid, grid.getPlayerPosition());
            if (!monster.getPosition().equals(initialPos)) {
                hasMoved = true;
                break;
            }
        }

        // Then
        assertTrue(hasMoved, "Monster should have moved at least once");
        assertTrue(grid.isValidPosition(monster.getPosition()), "Monster should remain within grid");
    }

    @Test
    void monstersShouldNotOccupySamePosition() {
        // Given
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(1, 2);
        Monster monster1 = new AggressiveMonster(pos1);
        Monster monster2 = new AggressiveMonster(pos2);

        monsterManager.addMonster(monster1);
        monsterManager.addMonster(monster2);

        // When
        monster2.move(grid, pos1); // Try to move to monster1's position

        // Then
        assertNotEquals(monster1.getPosition(), monster2.getPosition(),
                "Monsters should not occupy the same position");
    }
}