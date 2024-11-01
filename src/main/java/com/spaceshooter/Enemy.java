package com.spaceshooter;

public class Enemy {
    int x, y;
    private boolean isPowerUpEnemy;
    private PowerUpType powerUpType;
    private static final double POWER_UP_ENEMY_CHANCE = 0.1; // 10% chance for power-up enemy

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.isPowerUpEnemy = Math.random() < POWER_UP_ENEMY_CHANCE;
        if (isPowerUpEnemy) {
            // Randomly select a power-up type
            PowerUpType[] types = PowerUpType.values();
            this.powerUpType = types[(int)(Math.random() * types.length)];
        }
    }

    public boolean isPowerUpEnemy() {
        return isPowerUpEnemy;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}