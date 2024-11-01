// src/test/java/com/treasurehunt/monster/MonsterTest.java
package com.treasurehunt.monster;

import com.treasurehunt.Position;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

class MonsterTest {
    @Test
    void shouldCreateMonsterWithValidPosition() {
        Position position = new Position(1, 1);
        Monster monster = new AggressiveMonster(position);
        assertEquals(position, monster.getPosition());
    }
}