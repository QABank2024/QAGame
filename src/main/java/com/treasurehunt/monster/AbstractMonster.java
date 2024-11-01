// src/main/java/com/treasurehunt/monster/AbstractMonster.java
package com.treasurehunt.monster;

import com.treasurehunt.Position;

public abstract class AbstractMonster implements Monster {
    protected Position position;

    public AbstractMonster(Position position) {
        this.position = position;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public void setPosition(Position position) {
        this.position = position;
    }
}