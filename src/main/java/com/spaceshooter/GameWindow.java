package com.spaceshooter;// com.spaceshooter.GameWindow.java
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private UserManager userManager;

    public GameWindow() {
        super("Space Shooter");
        this.userManager = new UserManager();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add main menu and game panels
        mainPanel.add(new MainMenu(userManager, cardLayout, mainPanel), "menu");
        mainPanel.add(new SpaceShooter(userManager, cardLayout, mainPanel), "game");

        add(mainPanel);
        cardLayout.show(mainPanel, "menu");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}