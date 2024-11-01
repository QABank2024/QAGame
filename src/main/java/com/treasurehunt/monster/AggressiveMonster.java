// src/main/java/com/treasurehunt/monster/AggressiveMonster.java
package com.treasurehunt.monster;

import com.treasurehunt.Position;
import com.treasurehunt.Grid;

public class AggressiveMonster extends AbstractMonster {

    public AggressiveMonster(Position position) {
        super(position);
    }

    @Override
    public void move(Grid grid, Position playerPosition) {
        // If we're already at the player position, don't move
        if (position.equals(playerPosition)) {
            return;
        }

        // Calculate direction to move towards player
        int dx = Integer.compare(playerPosition.getX(), position.getX());
        int dy = Integer.compare(playerPosition.getY(), position.getY());

        // Try moving horizontally first
        Position horizontalMove = new Position(position.getX() + dx, position.getY());
        if (isValidMove(grid, horizontalMove)) {
            setPosition(horizontalMove);
            return;
        }

        // If horizontal move failed, try vertical
        Position verticalMove = new Position(position.getX(), position.getY() + dy);
        if (isValidMove(grid, verticalMove)) {
            setPosition(verticalMove);
            return;
        }
    }

    private boolean isValidMove(Grid grid, Position newPosition) {
        return grid.isValidPosition(newPosition) &&
                !MonsterManager.isPositionOccupiedByMonster(newPosition) &&
                !position.equals(newPosition);
    }

    @Override
    public String getEncounterMessage() {
        return "An aggressive monster catches you! Game Over!";
    }
}