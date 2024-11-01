// src/test/java/com/treasurehunt/monster/MonsterMovementTest.java
package com.treasurehunt.monster;

import com.treasurehunt.Grid;
import com.treasurehunt.Position;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class MonsterMovementTest {
    private Grid grid;

    @BeforeEach
    void setUp() {
        grid = new Grid(5, 5);
    }

    private void printPositions(String stage, Position monsterPos, Position playerPos) {
        System.out.println(stage + ":");
        System.out.println("Monster at: (" + monsterPos.getX() + "," + monsterPos.getY() + ")");
        System.out.println("Player at: (" + playerPos.getX() + "," + playerPos.getY() + ")");
    }

    @Test
    void aggressiveMonsterShouldMoveTowardPlayerHorizontally() {
        // Given
        Position monsterPos = new Position(0, 2);
        Position playerPos = new Position(4, 2);
        AggressiveMonster monster = new AggressiveMonster(monsterPos);
        Grid grid = new Grid(5, 5);

        printPositions("Before horizontal move", monsterPos, playerPos);

        // When
        monster.move(grid, playerPos);

        // Then
        Position newPos = monster.getPosition();
        printPositions("After horizontal move", newPos, playerPos);
        assertEquals(1, newPos.getX(), "Monster should move right toward player");
        assertEquals(2, newPos.getY(), "Monster should maintain same Y position");
    }

    @Test
    void aggressiveMonsterShouldMoveTowardPlayerVertically() {
        // Given
        Position monsterPos = new Position(2, 0);
        Position playerPos = new Position(2, 4);
        AggressiveMonster monster = new AggressiveMonster(monsterPos);
        Grid grid = new Grid(5, 5);

        printPositions("Before vertical move", monsterPos, playerPos);

        // When
        monster.move(grid, playerPos);

        // Then
        Position newPos = monster.getPosition();
        printPositions("After vertical move", newPos, playerPos);

        assertEquals(2, newPos.getX(), "Monster should maintain same X position");
        assertEquals(1, newPos.getY(), "Monster should move down toward player");
        assertTrue(newPos.calculateDistance(playerPos) < monsterPos.calculateDistance(playerPos),
                "Monster should be closer to player after move");
    }

    @Test
    void aggressiveMonsterShouldTakeAlternatePathWhenBlocked() {
        // Given
        Position monsterPos = new Position(0, 0);
        Position playerPos = new Position(1, 1);
        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // Set up MonsterManager to enable position checking
        MonsterManager monsterManager = new MonsterManager(grid);
        monsterManager.addMonster(monster);

        printPositions("Before alternate path move", monsterPos, playerPos);

        // When
        monster.move(grid, playerPos);
        Position newPos = monster.getPosition();

        printPositions("After alternate path move", newPos, playerPos);

        // Then
        boolean validMove = (newPos.getX() == 1 && newPos.getY() == 0) || // Moved right
                (newPos.getX() == 0 && newPos.getY() == 1);     // Moved down

        assertTrue(validMove,
                String.format("Monster should have moved right (1,0) or down (0,1), but was at %s",
                        newPos));

        int originalDistance = monsterPos.calculateDistance(playerPos); // Should be 2 (1+1)
        int newDistance = newPos.calculateDistance(playerPos);          // Should be 1 (0+1 or 1+0)

        System.out.println("Original distance: " + originalDistance);
        System.out.println("New distance: " + newDistance);

        assertTrue(newDistance < originalDistance,
                String.format("Monster should be closer to player after move. " +
                                "Original distance: %d, New distance: %d",
                        originalDistance, newDistance));
    }

    @Test
    void aggressiveMonsterShouldPreferHorizontalMove() {
        // Given
        Position monsterPos = new Position(0, 0);
        Position playerPos = new Position(1, 1);
        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // Set up MonsterManager
        MonsterManager monsterManager = new MonsterManager(grid);
        monsterManager.addMonster(monster);

        // When
        monster.move(grid, playerPos);
        Position newPos = monster.getPosition();

        // Then
        assertEquals(new Position(1, 0), newPos,
                "Monster should prefer horizontal movement when both options are available");
    }

    @Test
    void aggressiveMonsterShouldNotMoveWhenBlocked() {
        // Given
        Position monsterPos = new Position(0, 0);
        Position playerPos = new Position(1, 1);
        AggressiveMonster monster = new AggressiveMonster(monsterPos);

        // Set up MonsterManager with blocking monster
        MonsterManager monsterManager = new MonsterManager(grid);
        monsterManager.addMonster(monster);
        monsterManager.addMonster(new PassiveMonster(new Position(1, 0)));
        monsterManager.addMonster(new PassiveMonster(new Position(0, 1)));

        // When
        monster.move(grid, playerPos);
        Position newPos = monster.getPosition();

        // Then
        assertEquals(monsterPos, newPos,
                "Monster should stay in place when all valid moves are blocked");
    }
}