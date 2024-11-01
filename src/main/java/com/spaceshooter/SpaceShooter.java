package com.spaceshooter;// com.spaceshooter.SpaceShooter.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpaceShooter extends JPanel implements ActionListener, KeyListener, MouseMotionListener, MouseListener {
    private UserManager userManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel levelLabel;
    private JLabel scoreLabel;
    private JProgressBar expBar;
    private JPanel gamePanel;

    private int playerX = 300;
    private int playerY = 500;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;
    private boolean isShooting = false;
    private boolean isMouseControlled = false;
    private int targetX = playerX;
    private Rectangle bounds;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private Timer timer;
    private int score = 0;
    private boolean gameOver = false;
    private boolean isPaused = false;
    private Random random = new Random();

    // Movement constants
    private static final int KEYBOARD_SPEED = 5;
    private static final int PLAYER_WIDTH = 40;
    private static final int PLAYER_HEIGHT = 40;

    // Shooting mechanics
    private long lastShotTime = 0;
    private static final int SHOT_COOLDOWN = 100;
    private static final int BULLET_SPEED = 8;
    private static final int MAX_BULLETS = 200;
    private static final int BULLET_SPREAD = 10;

    // Game settings
    private static final Color PAUSE_OVERLAY = new Color(0, 0, 0, 127);
    private static final String PAUSE_MESSAGE = "PAUSED";
    private static final String PAUSE_INSTRUCTIONS = "Press P to Resume";
    private static final String CONTROLS_INFO = "Hold SPACE: Shoot | ←→/Mouse: Move | P: Pause | R: Restart";

    private static final int DEFAULT_WIDTH = 550;

    // Power Up Settings
    private ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private static final Color POWER_UP_ENEMY_COLOR = new Color(128, 0, 128); // Purple
    private static final int ORIGINAL_SHOT_COOLDOWN = 100;
    private static final Font POWER_UP_FONT = new Font("Arial", Font.BOLD, 16);

    public SpaceShooter(UserManager userManager, CardLayout cardLayout, JPanel mainPanel) {
        this.userManager = userManager;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        // Set main layout
        setLayout(new BorderLayout(0, 10));

        // Create game panel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGame(g);
            }
        };
        gamePanel.setBackground(Color.BLACK);
        gamePanel.setPreferredSize(new Dimension(600, 520));

        // Move player Y position to be relative to game panel
        playerY = 450;

        // Setup stats panel with GridLayout for more rows
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 10, 0));
        statsPanel.setBackground(Color.BLACK);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Level label
        levelLabel = new JLabel();
        levelLabel.setForeground(Color.WHITE);
        levelLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Score label
        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create bonus labels with default values
        JLabel multiplierLabel = new JLabel("Score: 1.0x");
        multiplierLabel.setForeground(Color.YELLOW);
        multiplierLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel expMultLabel = new JLabel("EXP: 1.0x");
        expMultLabel.setForeground(Color.YELLOW);
        expMultLabel.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel bulletLabel = new JLabel("Bullets: 1");
        bulletLabel.setForeground(Color.YELLOW);
        bulletLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Exp bar
        expBar = new JProgressBar(0, 100);
        expBar.setStringPainted(true);
        expBar.setPreferredSize(new Dimension(200, 20));

        // Add components to stats panel
        statsPanel.add(levelLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(multiplierLabel);
        statsPanel.add(expBar);
        statsPanel.add(bulletLabel);
        statsPanel.add(expMultLabel);

        // Add method to update the bonus labels
        Timer bonusUpdateTimer = new Timer(100, e -> {
            User currentUser = userManager.getCurrentUser();
            if (currentUser != null) {
                // Calculate score multiplier including power-up
                double scoreMultiplier = currentUser.getScoreMultiplier();
                if (isPowerUpActive(PowerUpType.DOUBLE_POINTS)) {
                    scoreMultiplier *= 2;
                }

                // Calculate exp multiplier including power-up
                double expMultiplier = currentUser.getExpMultiplier();
                if (isPowerUpActive(PowerUpType.DOUBLE_POINTS)) {
                    expMultiplier *= 2;
                }

                // Calculate bullet count including power-up
                int bulletCount = 1 + currentUser.getExtraBullets();
                if (isPowerUpActive(PowerUpType.MAX_POWER)) {
                    bulletCount *= 2;
                }

                // Update labels with current values including power-ups
                multiplierLabel.setText(String.format("Score: %.1fx", scoreMultiplier));
                expMultLabel.setText(String.format("EXP: %.1fx", expMultiplier));
                bulletLabel.setText(String.format("Bullets: %d", bulletCount));

                // Make labels yellow when powered up
                multiplierLabel.setForeground(isPowerUpActive(PowerUpType.DOUBLE_POINTS) ? Color.YELLOW : Color.WHITE);
                expMultLabel.setForeground(isPowerUpActive(PowerUpType.DOUBLE_POINTS) ? Color.YELLOW : Color.WHITE);
                bulletLabel.setForeground(isPowerUpActive(PowerUpType.MAX_POWER) ? Color.YELLOW : Color.WHITE);
            } else {
                // Default values when no user is selected
                multiplierLabel.setText("Score: 1.0x");
                expMultLabel.setText("EXP: 1.0x");
                bulletLabel.setText("Bullets: 1");

                // Reset colors to default
                multiplierLabel.setForeground(Color.WHITE);
                expMultLabel.setForeground(Color.WHITE);
                bulletLabel.setForeground(Color.WHITE);
            }
        });
        bonusUpdateTimer.start();

        // Add panels to main layout
        add(statsPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);

        // Setup input handlers
        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);
        gamePanel.addMouseMotionListener(this);
        gamePanel.addMouseListener(this);

        // Create timer but don't start it yet
        timer = new Timer(20, this);

        // Add component listener to handle initial setup after panel is visible
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                resetGame();  // This will initialize the game properly
                timer.start();  // Only start the timer when component is shown
                gamePanel.requestFocusInWindow();  // Make sure the panel has focus
            }
        });
    }

    // Add method to check for active power-ups
    private boolean isPowerUpActive(PowerUpType type) {
        return activePowerUps.stream()
                .anyMatch(p -> p.getType() == type && p.isActive());
    }

    private void initializeGame() {
        for (int i = 0; i < 5; i++) {
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        int maxX = gamePanel.getWidth();
        if (maxX <= 0) {
            maxX = DEFAULT_WIDTH;
        }
        int x = random.nextInt(Math.max(1, maxX - 30)); // Ensure positive bound and account for enemy width
        enemies.add(new Enemy(x, 0));
    }

    private void tryToShoot() {
        long currentTime = System.currentTimeMillis();
        int currentCooldown = isPowerUpActive(PowerUpType.RAPID_FIRE) ?
                ORIGINAL_SHOT_COOLDOWN / 2 : ORIGINAL_SHOT_COOLDOWN;

        if (currentTime - lastShotTime >= currentCooldown && bullets.size() < MAX_BULLETS) {
            User currentUser = userManager.getCurrentUser();
            int baseBullets = 1 + currentUser.getExtraBullets();
            int totalBullets = isPowerUpActive(PowerUpType.MAX_POWER) ?
                    baseBullets * 2 : baseBullets;

            // Calculate the total width of all bullets
            int totalWidth = (totalBullets - 1) * BULLET_SPREAD;
            int centerX = playerX + (PLAYER_WIDTH / 2);
            int startX = centerX - (totalWidth / 2);

            for (int i = 0; i < totalBullets; i++) {
                int bulletX = startX + (i * BULLET_SPREAD);
                bullets.add(new Bullet(bulletX, playerY));
            }

            lastShotTime = currentTime;
        }
    }

    private void updatePlayerPosition() {
        if (isMouseControlled) {
            playerX = targetX;
        } else {
            if (isMovingLeft && playerX > 0) playerX -= KEYBOARD_SPEED;
            if (isMovingRight && playerX < gamePanel.getWidth() - PLAYER_WIDTH) playerX += KEYBOARD_SPEED;
        }
        playerX = Math.max(0, Math.min(gamePanel.getWidth() - PLAYER_WIDTH, playerX));
    }

    private void drawGame(Graphics g) {
        if (!gameOver) {
            // Draw player
            g.setColor(Color.GREEN);
            g.fillRect(playerX, playerY, PLAYER_WIDTH, PLAYER_HEIGHT);

            // Draw bullets
            g.setColor(Color.YELLOW);
            for (Bullet bullet : bullets) {
                g.fillRect(bullet.x, bullet.y, 5, 10);
            }

            // Draw enemies (all the same color now)
            g.setColor(Color.RED);
            for (Enemy enemy : enemies) {
                g.fillRect(enemy.x, enemy.y, 30, 30);
            }

            // Draw controls info
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString(CONTROLS_INFO, 20, 580);

            // Draw active power-ups
            if (!activePowerUps.isEmpty()) {
                g.setFont(POWER_UP_FONT);
                int y = 100;
                for (PowerUp powerUp : activePowerUps) {
                    if (powerUp.isActive()) {
                        g.setColor(Color.YELLOW);
                        String text = String.format("%s: %.1fs",
                                powerUp.getType().getDisplayName(),
                                powerUp.getRemainingTime());
                        g.drawString(text, 20, y);
                        y += 25;
                    }
                }
            }

            // Update stats
            User currentUser = userManager.getCurrentUser();
            levelLabel.setText(String.format("Level: %d (Prestige %d)",
                    currentUser.getCurrentLevel(),
                    currentUser.getPrestigeLevel()));
            scoreLabel.setText(String.format("Score: %d", score));
            expBar.setValue((int)(currentUser.getProgressToNextLevel() * 100));
            expBar.setString(String.format("EXP: %d/%d",
                    currentUser.getCurrentExp(),
                    currentUser.getExpForNextLevel()));

            if (isPaused) {
                drawPauseOverlay(g);
            }
        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over!", 200, 250);
            g.drawString("Final Score: " + score, 190, 300);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press R to restart", 230, 350);
        }
    }

    private void drawPauseOverlay(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(PAUSE_OVERLAY);
        g2d.fillRect(0, 0, gamePanel.getWidth(), gamePanel.getHeight());

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (gamePanel.getWidth() - metrics.stringWidth(PAUSE_MESSAGE)) / 2;
        int y = gamePanel.getHeight() / 2;
        g2d.drawString(PAUSE_MESSAGE, x, y);

        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        metrics = g2d.getFontMetrics();
        x = (gamePanel.getWidth() - metrics.stringWidth(PAUSE_INSTRUCTIONS)) / 2;
        y = y + 40;
        g2d.drawString(PAUSE_INSTRUCTIONS, x, y);
    }

    private void handleScoreAndExp(int points) {
        User currentUser = userManager.getCurrentUser();
        double scoreMultiplier = currentUser.getScoreMultiplier();
        if (isPowerUpActive(PowerUpType.DOUBLE_POINTS)) {
            scoreMultiplier *= 2;
        }

        int multipliedScore = (int)(points * scoreMultiplier);
        score += multipliedScore;
        currentUser.setHighScore(score);

        // Apply double points to exp too
        int expPoints = points * 100;
        if (isPowerUpActive(PowerUpType.DOUBLE_POINTS)) {
            expPoints *= 2;
        }
        currentUser.addExp(expPoints);
        userManager.saveUsers();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver && !isPaused) {
            activePowerUps.removeIf(p -> !p.isActive());
            if (isShooting) {
                tryToShoot();
            }

            updatePlayerPosition();

            // Move bullets
            ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
            for (Bullet bullet : bullets) {
                bullet.y -= BULLET_SPEED;
                if (bullet.y < 0) bulletsToRemove.add(bullet);
            }
            bullets.removeAll(bulletsToRemove);

            // Move enemies
            ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
            for (Enemy enemy : enemies) {
                enemy.y += 3;
                int maxHeight = gamePanel.getHeight();
                if (maxHeight <= 0) maxHeight = 520; // Default height
                if (enemy.y > maxHeight) {
                    enemiesToRemove.add(enemy);
                    gameOver = true;
                }
            }
            enemies.removeAll(enemiesToRemove);

            // Check collisions
            for (Bullet bullet : bullets) {
                for (Enemy enemy : enemies) {
                    if (bullet.intersects(enemy)) {
                        bulletsToRemove.add(bullet);
                        enemiesToRemove.add(enemy);

                        // Handle power-up drops
                        if (enemy.isPowerUpEnemy()) {
                            PowerUpType dropType = enemy.getPowerUpType();
                            if (!isPowerUpActive(dropType)) {
                                activePowerUps.add(new PowerUp(dropType));
                            }
                        }

                        handleScoreAndExp(100);
                    }
                }
            }
            bullets.removeAll(bulletsToRemove);
            enemies.removeAll(enemiesToRemove);

            // Spawn new enemies
            if (enemies.size() < 5) {
                spawnEnemy();
            }
        }
        gamePanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isPaused && !gameOver && gamePanel.getBounds().contains(e.getPoint())) {
            isMouseControlled = true;
            targetX = Math.max(0, Math.min(gamePanel.getWidth() - PLAYER_WIDTH,
                    e.getX() - PLAYER_WIDTH / 2));
            playerX = targetX;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        isMouseControlled = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!isPaused && !gameOver) {
            isMouseControlled = true;
            targetX = e.getX() - PLAYER_WIDTH / 2;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                isMovingLeft = true;
                isMouseControlled = false;
                break;
            case KeyEvent.VK_RIGHT:
                isMovingRight = true;
                isMouseControlled = false;
                break;
            case KeyEvent.VK_SPACE:
                if (!isPaused && !gameOver) {
                    isShooting = true;
                    tryToShoot();
                }
                break;
            case KeyEvent.VK_P:
                if (!gameOver) {
                    isPaused = !isPaused;
                }
                break;
            case KeyEvent.VK_R:
                if (gameOver) {
                    resetGame();
                }
                break;
            case KeyEvent.VK_ESCAPE:
                cardLayout.show(mainPanel, "menu");
                resetGame();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                isMovingLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                isMovingRight = false;
                break;
            case KeyEvent.VK_SPACE:
                isShooting = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

    private void resetGame() {
        playerX = gamePanel.getWidth() / 2 - PLAYER_WIDTH / 2;  // Center the player
        playerY = 450;
        targetX = playerX;
        bullets.clear();
        enemies.clear();
        score = 0;
        gameOver = false;
        isPaused = false;
        isShooting = false;
        isMouseControlled = false;
        lastShotTime = 0;
        initializeGame();  // This spawns the initial enemies
    }
}