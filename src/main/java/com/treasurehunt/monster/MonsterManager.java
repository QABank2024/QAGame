// src/main/java/com/treasurehunt/monster/MonsterManager.java
package com.treasurehunt.monster;

import com.treasurehunt.Grid;
import com.treasurehunt.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterManager {
    private final List<Monster> monsters;
    private final Grid grid;
    private final Random random;
    private static MonsterManager instance;
    private int aggressiveCount = 0;
    private int passiveCount = 0;

    public MonsterManager(Grid grid) {
        this.grid = grid;
        this.monsters = new ArrayList<>();
        this.random = new Random();
        instance = this;
    }

    public void initializeMonsters(int count) {
        monsters.clear();
        aggressiveCount = 0;
        passiveCount = 0;

        for (int i = 0; i < count; i++) {
            Monster monster = createRandomMonster();
            if (monster != null) {
                monsters.add(monster);
            }
        }

        // If we have monsters but don't have both types, force creation of missing type
        if (!monsters.isEmpty() && (aggressiveCount == 0 || passiveCount == 0)) {
            Position pos = findValidPosition();
            if (pos != null) {
                Monster monster;
                if (aggressiveCount == 0) {
                    monster = new AggressiveMonster(pos);
                    aggressiveCount++;
                } else {
                    monster = new PassiveMonster(pos);
                    passiveCount++;
                }
                monsters.add(monster);
            }
        }
    }

    private Monster createRandomMonster() {
        Position position = findValidPosition();
        if (position == null) {
            return null;
        }

        // Ensure we get a mix of both types
        boolean createAggressive;
        if (aggressiveCount == 0) {
            createAggressive = true;
        } else if (passiveCount == 0) {
            createAggressive = false;
        } else {
            createAggressive = random.nextBoolean();
        }

        Monster monster;
        if (createAggressive) {
            monster = new AggressiveMonster(position);
            aggressiveCount++;
        } else {
            monster = new PassiveMonster(position);
            passiveCount++;
        }

        return monster;
    }

    private Position findValidPosition() {
        int attempts = 0;
        final int MAX_ATTEMPTS = 100;

        while (attempts < MAX_ATTEMPTS) {
            Position pos = new Position(
                    random.nextInt(grid.getWidth()),
                    random.nextInt(grid.getHeight())
            );

            if (!isPositionOccupied(pos)) {
                return pos;
            }
            attempts++;
        }

        // If random attempts failed, try systematic search
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Position pos = new Position(x, y);
                if (!isPositionOccupied(pos)) {
                    return pos;
                }
            }
        }

        return null;
    }

    public void addMonster(Monster monster) {
        if (monster != null && grid.isValidPosition(monster.getPosition())
                && !isMonsterAtPosition(monster.getPosition())) {
            monsters.add(monster);
        }
    }

    private boolean isPositionOccupied(Position position) {
        return position.equals(grid.getPlayerPosition()) ||
                position.equals(grid.getTreasurePosition()) ||
                isMonsterAtPosition(position);
    }

    public boolean isMonsterAtPosition(Position position) {
        return monsters.stream()
                .anyMatch(monster -> monster.getPosition().equals(position));
    }

    // Static method for monsters to check positions
    public static boolean isPositionOccupiedByMonster(Position position) {
        return instance != null && instance.isMonsterAtPosition(position);
    }

    public List<Monster> getMonsters() {
        return new ArrayList<>(monsters);
    }

    public void updateMonsters() {
        List<Position> originalPositions = monsters.stream()
                .map(Monster::getPosition)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Update each monster
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            Position originalPos = monster.getPosition();
            monster.move(grid, grid.getPlayerPosition());

            // If new position conflicts with another monster, revert
            if (monsters.stream()
                    .filter(m -> m != monster)
                    .anyMatch(m -> m.getPosition().equals(monster.getPosition()))) {
                monster.setPosition(originalPos);
            }
        }
    }
}