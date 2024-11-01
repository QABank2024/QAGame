// src/test/java/com/treasurehunt/GridTest.java
package com.treasurehunt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GridTest {
    @Test
    void shouldCreateGridWithSpecifiedDimensions() {
        Grid grid = new Grid(5, 5);
        assertEquals(5, grid.getWidth());
        assertEquals(5, grid.getHeight());
    }

    @Test
    void shouldThrowExceptionForInvalidDimensions() {
        assertThrows(IllegalArgumentException.class, () -> new Grid(0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Grid(5, 0));
    }

    @Test
    void shouldPlacePlayerInValidPosition() {
        Grid grid = new Grid(5, 5);
        Position position = new Position(2, 2);
        grid.setPlayerPosition(position);
        assertEquals(position, grid.getPlayerPosition());
    }

    @Test
    void shouldValidatePositionSuccessfully() {
        Grid grid = new Grid(5, 5);
        Position validPosition = new Position(2, 2);

        assertDoesNotThrow(() -> grid.validatePosition(validPosition));
        assertTrue(grid.isValidPosition(validPosition));
    }

    @Test
    void shouldRejectInvalidPosition() {
        Grid grid = new Grid(5, 5);
        Position invalidPosition = new Position(5, 5);

        assertThrows(IllegalArgumentException.class,
                () -> grid.validatePosition(invalidPosition));
        assertFalse(grid.isValidPosition(invalidPosition));
    }

    @Test
    void shouldRejectNullPosition() {
        Grid grid = new Grid(5, 5);

        assertThrows(IllegalArgumentException.class,
                () -> grid.validatePosition(null));
        assertFalse(grid.isValidPosition(null));
    }
}