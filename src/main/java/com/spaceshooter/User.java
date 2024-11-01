package com.spaceshooter;// com.spaceshooter.User.java
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private int highScore;
    private int currentLevel;
    private int currentExp;
    private int prestigeLevel;
    private static final int MAX_LEVEL = 50;
    private static final int BASE_EXP = 500;  // Base experience needed for level 1->2
    private static final double EXP_SCALING = 1.2;  // Each level requires 20% more exp than the last

    // Prestige bonuses
    private static final double SCORE_MULTIPLIER_PER_PRESTIGE = 0.2; // +20% score per prestige
    private static final double EXP_MULTIPLIER_PER_PRESTIGE = 0.15; // +15% exp per prestige
    private static final int BULLET_BONUS_PER_PRESTIGE = 1; // +1 bullet every 2 prestige levels

    public User(String username) {
        this.username = username;
        this.highScore = 0;
        this.currentLevel = 1;
        this.currentExp = 0;
        this.prestigeLevel = 0;
    }

    public void addExp(int exp) {
        // Apply exp multiplier from prestige
        exp = (int)(exp * getExpMultiplier());

        currentExp += exp;
        while (currentExp >= getExpForNextLevel() && currentLevel < MAX_LEVEL) {
            currentExp -= getExpForNextLevel();
            currentLevel++;
        }
        // Cap exp at max needed for current level
        if (currentLevel == MAX_LEVEL && currentExp > getExpForNextLevel()) {
            currentExp = getExpForNextLevel();
        }
    }

    public int getExpForNextLevel() {
        // Uses a geometric progression formula for exp requirements
        return (int)(BASE_EXP * Math.pow(EXP_SCALING, currentLevel - 1));
    }

    // Helper method to get total exp needed from level 1 to current level
    public int getTotalExpToLevel() {
        int total = 0;
        for (int i = 1; i < currentLevel; i++) {
            total += BASE_EXP * Math.pow(EXP_SCALING, i - 1);
        }
        return total;
    }

    public boolean canPrestige() {
        return currentLevel == MAX_LEVEL;
    }

    public void prestige() {
        if (canPrestige()) {
            prestigeLevel++;
            currentLevel = 1;
            currentExp = 0;
        }
    }

    // Prestige bonus getters
    public double getScoreMultiplier() {
        return 1.0 + (prestigeLevel * SCORE_MULTIPLIER_PER_PRESTIGE);
    }

    public double getExpMultiplier() {
        return 1.0 + (prestigeLevel * EXP_MULTIPLIER_PER_PRESTIGE);
    }

    public int getExtraBullets() {
        return prestigeLevel / 2; // One extra bullet every 2 prestige levels
    }

    // Regular getters and setters
    public String getUsername() { return username; }
    public int getHighScore() { return highScore; }
    public void setHighScore(int score) {
        if (score > highScore) highScore = score;
    }
    public int getCurrentLevel() { return currentLevel; }
    public int getCurrentExp() { return currentExp; }
    public int getPrestigeLevel() { return prestigeLevel; }
    public double getProgressToNextLevel() {
        return (double)currentExp / getExpForNextLevel();
    }
}