package com.spaceshooter;

// com.spaceshooter.PowerUpType.java
public enum PowerUpType {
    DOUBLE_POINTS("Double Points", "2x Score & XP"),
    RAPID_FIRE("Rapid Fire", "2x Fire Rate"),
    MAX_POWER("Max Power", "2x Bullets");

    private final String displayName;
    private final String description;

    PowerUpType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return displayName;
    }
}