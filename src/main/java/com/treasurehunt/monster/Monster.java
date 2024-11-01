// src/main/java/com/treasurehunt/monster/Monster.java
package com.treasurehunt.monster;

import com.treasurehunt.Position;
import com.treasurehunt.Grid;

public interface Monster {
    Position getPosition();
    void setPosition(Position position);
    void move(Grid grid, Position playerPosition);
    String getEncounterMessage();
}