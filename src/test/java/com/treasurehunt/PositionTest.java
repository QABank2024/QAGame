// src/test/java/com/treasurehunt/PositionTest.java
package com.treasurehunt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    @Test
    void shouldCalculateDistanceCorrectly() {
        // Given
        Position p1 = new Position(0, 0);
        Position p2 = new Position(3, 4);

        // When
        int distance = p1.calculateDistance(p2);

        // Then - Manhattan distance should be |3-0| + |4-0| = 7
        assertEquals(7, distance,
                "Manhattan distance should be sum of x and y differences");
    }

    @Test
    void shouldCalculateDistanceSymmetrically() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(4, 5);

        assertEquals(p1.calculateDistance(p2), p2.calculateDistance(p1),
                "Distance should be the same regardless of direction");
    }

    @Test
    void shouldCalculateZeroDistanceForSamePosition() {
        Position p = new Position(3, 3);
        assertEquals(0, p.calculateDistance(p),
                "Distance to self should be 0");
    }

    @Test
    void shouldCalculateHorizontalDistance() {
        Position p1 = new Position(0, 5);
        Position p2 = new Position(3, 5);
        assertEquals(3, p1.calculateDistance(p2),
                "Horizontal distance should be difference in x coordinates");
    }

    @Test
    void shouldCalculateVerticalDistance() {
        Position p1 = new Position(5, 0);
        Position p2 = new Position(5, 4);
        assertEquals(4, p1.calculateDistance(p2),
                "Vertical distance should be difference in y coordinates");
    }

    @Test
    void shouldImplementEqualsCorrectly() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 1);
        Position p3 = new Position(1, 2);

        assertEquals(p1, p2, "Same coordinates should be equal");
        assertNotEquals(p1, p3, "Different coordinates should not be equal");
        assertNotEquals(p1, null, "Position should not equal null");
        assertNotEquals(p1, "not a position", "Position should not equal other types");
    }

    @Test
    void shouldGenerateConsistentHashCode() {
        Position p1 = new Position(1, 1);
        Position p2 = new Position(1, 1);

        assertEquals(p1.hashCode(), p2.hashCode(),
                "Equal positions should have equal hash codes");
    }

    @Test
    void shouldProduceCorrectStringRepresentation() {
        Position p = new Position(3, 4);
        assertEquals("(3,4)", p.toString(),
                "toString should return coordinates in (x,y) format");
    }
}