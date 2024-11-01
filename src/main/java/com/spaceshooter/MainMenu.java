package com.spaceshooter;// com.spaceshooter.MainMenu.java
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JPanel {
    private UserManager userManager;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JLabel welcomeLabel;

    public MainMenu(UserManager userManager, CardLayout cardLayout, JPanel mainPanel) {
        this.userManager = userManager;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(25, 25, 35));  // Dark blue-grey background
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        initializeUI();
    }

    private void initializeUI() {
        // Title Panel
        JPanel titlePanel = createTitlePanel();
        add(titlePanel, BorderLayout.NORTH);

        // Center Panel (com.spaceshooter.User Selection and Main Buttons)
        JPanel centerPanel = createCenterPanel();
        add(centerPanel, BorderLayout.CENTER);

        // Stats Panel
        JPanel statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.EAST);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("SPACE SHOOTER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        welcomeLabel = new JLabel("Welcome, Commander");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 18));
        welcomeLabel.setForeground(Color.LIGHT_GRAY);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(welcomeLabel, BorderLayout.CENTER);

        return titlePanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20));

        // com.spaceshooter.User Selection Panel
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setOpaque(false);
        userPanel.setBorder(createPanelBorder("com.spaceshooter.User Selection"));

        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(300, 30));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton selectUserBtn = createStyledButton("Select/Create com.spaceshooter.User");
        selectUserBtn.addActionListener(e -> handleUserSelection());

        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(usernameField);
        userPanel.add(Box.createVerticalStrut(10));
        userPanel.add(selectUserBtn);
        userPanel.add(Box.createVerticalStrut(10));

        // Main Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(createPanelBorder("Game Options"));

        JButton playButton = createStyledButton("Play Game");
        playButton.addActionListener(e -> handlePlayGame());

        JButton leaderboardButton = createStyledButton("Leaderboard");
        leaderboardButton.addActionListener(e -> showLeaderboard());

        JButton prestigeButton = createStyledButton("Prestige");
        prestigeButton.addActionListener(e -> handlePrestige());

        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(playButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(leaderboardButton);
        buttonsPanel.add(Box.createVerticalStrut(10));
        buttonsPanel.add(prestigeButton);
        buttonsPanel.add(Box.createVerticalStrut(10));

        centerPanel.add(userPanel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(buttonsPanel);

        return centerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(createPanelBorder("Current Stats"));
        statsPanel.setPreferredSize(new Dimension(250, 0));

        // Stats will be updated when user is selected
        JLabel levelLabel = new JLabel("Level: -");
        JLabel prestigeLabel = new JLabel("Prestige: -");
        JLabel scoreLabel = new JLabel("High Score: -");
        JLabel expLabel = new JLabel("EXP: -");

        // Style the labels
        Component[] labels = {levelLabel, prestigeLabel, scoreLabel, expLabel};
        for (Component label : labels) {
            ((JLabel)label).setForeground(Color.WHITE);
            ((JLabel)label).setFont(new Font("Arial", Font.PLAIN, 16));
            ((JLabel)label).setAlignmentX(Component.CENTER_ALIGNMENT);
            statsPanel.add(label);
            statsPanel.add(Box.createVerticalStrut(10));
        }

        // Update stats when user is selected
        Timer statsTimer = new Timer(100, e -> {
            User currentUser = userManager.getCurrentUser();
            if (currentUser != null) {
                levelLabel.setText(String.format("Level: %d", currentUser.getCurrentLevel()));
                prestigeLabel.setText(String.format("Prestige: %d", currentUser.getPrestigeLevel()));
                scoreLabel.setText(String.format("High Score: %d", currentUser.getHighScore()));
                expLabel.setText(String.format("EXP: %d/%d",
                        currentUser.getCurrentExp(),
                        currentUser.getExpForNextLevel()));
            } else {
                levelLabel.setText("Level: -");
                prestigeLabel.setText("Prestige: -");
                scoreLabel.setText("High Score: -");
                expLabel.setText("EXP: -");
            }
        });
        statsTimer.start();

        return statsPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(45, 45, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        button.setMaximumSize(new Dimension(300, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(65, 65, 75));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(45, 45, 55));
            }
        });

        return button;
    }

    private Border createPanelBorder(String title) {
        Border line = BorderFactory.createLineBorder(new Color(70, 70, 80));
        Border empty = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createCompoundBorder(line, empty), title);
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 18));
        titledBorder.setTitleColor(Color.WHITE);
        return titledBorder;
    }

    private void handleUserSelection() {
        String username = usernameField.getText().trim();
        if (!username.isEmpty()) {
            userManager.setCurrentUser(username);
            welcomeLabel.setText("Welcome, Commander " + username);
            JOptionPane.showMessageDialog(this, "com.spaceshooter.User selected: " + username);
        }
    }

    private void handlePlayGame() {
        if (userManager.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user first!",
                    "No com.spaceshooter.User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        cardLayout.show(mainPanel, "game");
        mainPanel.getComponent(1).requestFocusInWindow();
    }

    private void handlePrestige() {
        User currentUser = userManager.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a user first!",
                    "No com.spaceshooter.User Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (currentUser.canPrestige()) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to prestige?\nThis will reset your level to 1 but grant permanent bonuses!",
                    "Prestige Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                currentUser.prestige();
                JOptionPane.showMessageDialog(this,
                        "Prestige level increased to " + currentUser.getPrestigeLevel() + "!",
                        "Prestige Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Must reach level 50 to prestige!",
                    "Cannot Prestige",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showLeaderboard() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='width: 400px; padding: 10px;'>");
        sb.append("<h1 style='text-align: center; color: #FFD700;'>Leaderboard</h1>");
        sb.append("<table style='width: 100%;'>");

        int rank = 1;
        for (User user : userManager.getLeaderboard()) {
            String rowColor = rank % 2 == 0 ? "#2A2A2A" : "#353535";
            sb.append(String.format("<tr style='background-color: %s;'>", rowColor));
            sb.append(String.format("<td style='padding: 5px; color: white;'>%d.</td>", rank++));
            sb.append(String.format("<td style='padding: 5px; color: white;'>%s</td>", user.getUsername()));
            sb.append(String.format("<td style='padding: 5px; color: #FFD700;'>Prestige %d</td>", user.getPrestigeLevel()));
            sb.append(String.format("<td style='padding: 5px; color: #90EE90;'>Level %d</td>", user.getCurrentLevel()));
            sb.append(String.format("<td style='padding: 5px; color: #ADD8E6;'>Score: %d</td>", user.getHighScore()));
            sb.append("</tr>");
        }

        sb.append("</table></body></html>");

        JLabel leaderboardLabel = new JLabel(sb.toString());
        JScrollPane scrollPane = new JScrollPane(leaderboardLabel);
        scrollPane.setPreferredSize(new Dimension(500, 400));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Leaderboard",
                JOptionPane.PLAIN_MESSAGE);
    }
}