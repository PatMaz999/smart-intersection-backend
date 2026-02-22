package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClearancePhaseTest {

    @Mock
    private LightsState previousState;

    @Test
    void shouldTransformNorthSouthAxis() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.GREEN,
                Direction.SOUTH, LightColor.GREEN,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.RED, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldTransformEastWestAxis() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.GREEN,
                Direction.WEST, LightColor.GREEN
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.RED, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldTransformSingleNorth() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.GREEN,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);

        // then
        assertOnlyOneIsYellow(phase.getLightColors(), Direction.NORTH);
    }

    @Test
    void shouldTransformSingleSouth() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.GREEN,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);

        // then
        assertOnlyOneIsYellow(phase.getLightColors(), Direction.SOUTH);
    }

    @Test
    void shouldTransformSingleEast() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.GREEN,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);

        // then
        assertOnlyOneIsYellow(phase.getLightColors(), Direction.EAST);
    }

    @Test
    void shouldTransformSingleWest() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.GREEN
        ));

        // when
        ClearancePhase phase = new ClearancePhase(previousState);

        // then
        assertOnlyOneIsYellow(phase.getLightColors(), Direction.WEST);
    }

    @Test
    void canMoveShouldAlwaysReturnFalse() {
        when(previousState.getLightColors()).thenReturn(Map.of(Direction.NORTH, LightColor.GREEN));
        ClearancePhase phase = new ClearancePhase(previousState);
        assertFalse(phase.canMove(null, Direction.NORTH));
    }

    private void assertOnlyOneIsYellow(Map<Direction, LightColor> result, Direction yellowDir) {
        assertAll(
                () -> assertEquals(LightColor.YELLOW, result.get(yellowDir)),
                () -> result.forEach((dir, color) -> {
                    if (dir != yellowDir)
                        assertEquals(LightColor.RED, color);
                })
        );
    }
}