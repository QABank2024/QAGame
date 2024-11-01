package com.spaceshooter;

// com.spaceshooter.PowerUp.java
public class PowerUp {
    private PowerUpType type;
    private long startTime;
    private static final long DURATION = 30000; // 30 seconds in milliseconds

    public PowerUp(PowerUpType type) {
        this.type = type;
        this.startTime = System.currentTimeMillis();
    }

    public boolean isActive() {
        return System.currentTimeMillis() - startTime < DURATION;
    }

    public double getRemainingTime() {
        return Math.max(0, DURATION - (System.currentTimeMillis() - startTime)) / 1000.0;
    }

    public PowerUpType getType() {
        return type;
    }
}