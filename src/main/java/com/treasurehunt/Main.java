// src/main/java/com/treasurehunt/Main.java
package com.treasurehunt;

import com.treasurehunt.renderer.GameRenderer;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GameController controller = new GameController();
        GameRenderer renderer = new GameRenderer();

        System.out.println("Welcome to Treasure Hunt!");
        System.out.println("Enter grid width, height, and number of monsters (e.g., '5 5 2'):");

        String[] setup = scanner.nextLine().split(" ");
        if (setup.length != 3) {
            System.out.println("Invalid input. Please provide width, height, and monster count.");
            return;
        }

        try {
            int width = Integer.parseInt(setup[0]);
            int height = Integer.parseInt(setup[1]);
            int monsters = Integer.parseInt(setup[2]);

            if (!controller.initializeGame(width, height, monsters)) {
                System.out.println("Invalid game parameters. Please try again.");
                return;
            }

            System.out.println("Game started! Use 'move <direction>' commands (e.g., 'move right')");
            System.out.println("Available directions: up, down, left, right");

            while (!controller.getGameStatus().isGameOver()) {
                // Clear console (platform-dependent)
                System.out.print("\033[H\033[2J");
                System.out.flush();

                // Display game state
                GameService gameService = controller.getGameService();
                System.out.println(renderer.render(gameService.getGrid(), gameService.getMonsterManager()));
                System.out.println("\n" + controller.getGameStatus().getMessage());

                System.out.print("Enter command: ");
                String command = scanner.nextLine();

                if (!controller.processCommand(command)) {
                    System.out.println("Invalid command. Use 'move <direction>'");
                }

                // Small delay to make monster movements visible
                Thread.sleep(100);
            }

            // Display final game state
            GameService gameService = controller.getGameService();
            System.out.println(renderer.render(gameService.getGrid(), gameService.getMonsterManager()));
            System.out.println("\n" + controller.getGameStatus().getMessage());

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numbers for width, height, and monsters.");
        } catch (InterruptedException e) {
            System.out.println("Game interrupted.");
        }
    }
}