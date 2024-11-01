// src/main/java/com/treasurehunt/monster/PassiveMonster.java
package com.treasurehunt.monster;

import com.treasurehunt.Position;
import com.treasurehunt.Grid;
import java.util.Random;

public class PassiveMonster extends AbstractMonster {
    private final Random random = new Random();

    public PassiveMonster(Position position) {
        super(position);
    }

    @Override
    public void move(Grid grid, Position playerPosition) {
        // Random movement: -1 (move back), 0 (stay), 1 (move forward)
        int[] possibleMoves = {-1, 0, 1};

        // Try up to 4 times to find a valid random move
        for (int attempts = 0; attempts < 4; attempts++) {
            // Randomly choose movement in x and y directions
            int dx = possibleMoves[random.nextInt(possibleMoves.length)];
            int dy = possibleMoves[random.nextInt(possibleMoves.length)];

            // Create new position
            Position newPosition = new Position(
                    position.getX() + dx,
                    position.getY() + dy
            );

            // If the move is valid and no monster is there, make it
            if (grid.isValidPosition(newPosition) &&
                    !MonsterManager.isPositionOccupiedByMonster(newPosition)) {
                setPosition(newPosition);
                return;
            }
        }
        // If no valid move is found after 4 attempts, stay in current position
    }

    @Override
    public String getEncounterMessage() {
        return "A passive monster blocks your path! Game Over!";
    }
}