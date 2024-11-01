package com.spaceshooter;

// com.spaceshooter.Bullet.java
public class Bullet {
    int x, y;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean intersects(Enemy enemy) {
        return x < enemy.x + 30 && x + 5 > enemy.x &&
                y < enemy.y + 30 && y + 10 > enemy.y;
    }
}