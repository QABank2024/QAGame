// src/test/java/com/treasurehunt/renderer/GameRendererTest.java
package com.treasurehunt.renderer;

import com.treasurehunt.Grid;
import com.treasurehunt.Position;
import com.treasurehunt.monster.Monster;
import com.treasurehunt.monster.MonsterManager;
import com.treasurehunt.monster.AggressiveMonster;
import com.treasurehunt.monster.PassiveMonster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameRendererTest {
    private GameRenderer renderer;
    private Grid grid;
    private MonsterManager monsterManager;

    @BeforeEach
    void setUp() {
        renderer = new GameRenderer();
        grid = new Grid(3, 3);
        monsterManager = new MonsterManager(grid);
    }

    @Test
    void shouldRenderEmptyGrid() {
        // Given
        grid.setPlayerPosition(new Position(0, 0));
        grid.setTreasurePosition(new Position(2, 2));

        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        String expected = String.join("\n",
                "┌───┬───┬───┐",
                "│ P │ · │ · │",
                "├───┼───┼───┤",
                "│ · │ · │ · │",
                "├───┼───┼───┤",
                "│ · │ · │ T │",
                "└───┴───┴───┘"
        ) + "\n";

        assertTrue(result.startsWith(expected),
                "Grid should be rendered correctly\nExpected:\n" + expected + "\nActual:\n" + result);
    }

    @Test
    void shouldRenderGridWithAllEntityTypes() {
        // Given
        grid.setPlayerPosition(new Position(0, 0));
        grid.setTreasurePosition(new Position(2, 2));

        // Create and place monsters
        Monster aggressiveMonster = new AggressiveMonster(new Position(1, 0));
        Monster passiveMonster = new PassiveMonster(new Position(1, 1));

        monsterManager.addMonster(aggressiveMonster);
        monsterManager.addMonster(passiveMonster);

        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        String expected = String.join("\n",
                "┌───┬───┬───┐",
                "│ P │ A │ · │",
                "├───┼───┼───┤",
                "│ · │ M │ · │",
                "├───┼───┼───┤",
                "│ · │ · │ T │",
                "└───┴───┴───┘"
        ) + "\n";

        assertTrue(result.startsWith(expected),
                "Grid should contain all entity types\nExpected:\n" + expected + "\nActual:\n" + result);
    }

    @Test
    void shouldShowLastPlacedEntityWhenPositionsOverlap() {
        // Given
        Position overlappingPosition = new Position(1, 1);
        grid.setPlayerPosition(overlappingPosition);
        grid.setTreasurePosition(overlappingPosition);

        // Create and add the monster - should be rendered on top
        Monster monster = new AggressiveMonster(overlappingPosition);
        monsterManager.addMonster(monster);

        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        // Print the actual result for debugging
        System.out.println("Actual rendered output:");
        System.out.println(result);

        String[] lines = result.split("\n");
        boolean foundExpectedLine = false;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("│ · │ A │ · │")) {
                foundExpectedLine = true;
                break;
            }
        }

        assertTrue(foundExpectedLine,
                String.format("Should show monster 'A' at position (1,1)\nExpected to find line containing: │ · │ A │ · │\nActual output:\n%s",
                        result));
    }

    @Test
    void shouldRenderEmptyCellsWhenNoEntitiesPresent() {
        // Given - empty grid with no entities set
        grid = new Grid(2, 2);
        monsterManager = new MonsterManager(grid);

        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        String expected = String.join("\n",
                "┌───┬───┐",
                "│ · │ · │",
                "├───┼───┤",
                "│ · │ · │",
                "└───┴───┘"
        ) + "\n";

        assertTrue(result.startsWith(expected),
                "Empty cells should be rendered with dots\nExpected:\n" + expected + "\nActual:\n" + result);
    }

    @Test
    void shouldRenderCorrectBorderIntersections() {
        // Given
        grid = new Grid(2, 2);
        monsterManager = new MonsterManager(grid);

        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        // Create an array of expected characters and their descriptions
        String[][] expectedChars = {
                {"┌", "top-left corner"},
                {"┐", "top-right corner"},
                {"└", "bottom-left corner"},
                {"┘", "bottom-right corner"},
                {"├", "left T-junction"},
                {"┤", "right T-junction"},
                {"┬", "top T-junction"},
                {"┴", "bottom T-junction"},
                {"┼", "cross intersection"}
        };

        for (String[] pair : expectedChars) {
            assertTrue(result.contains(pair[0]),
                    "Should have " + pair[1] + " (" + pair[0] + ")");
        }
    }

    @Test
    void shouldIncludeAllLegendElements() {
        // When
        String result = renderer.render(grid, monsterManager);

        // Then
        String[] expectedLegendItems = {
                "P - Player",
                "T - Treasure",
                "A - Aggressive Monster",
                "M - Passive Monster",
                "· - Empty Space"
        };

        for (String legendItem : expectedLegendItems) {
            assertTrue(result.contains(legendItem),
                    "Legend should contain item: " + legendItem);
        }
    }
}