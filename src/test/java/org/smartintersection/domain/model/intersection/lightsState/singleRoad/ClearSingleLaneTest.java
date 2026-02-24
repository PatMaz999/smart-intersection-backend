package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClearSingleLaneTest {

    @Mock
    private LightsState previousState;

    @Test
    void shouldClearNorthWhenNorthAndSouthAreGreen() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.GREEN,
                Direction.SOUTH, LightColor.GREEN,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearSingleLane phase = new ClearSingleLane(previousState, Direction.NORTH);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.GREEN, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.RED, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldClearEastWhenEastAndWestAreGreen() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.GREEN,
                Direction.WEST, LightColor.GREEN
        ));

        // when
        ClearSingleLane phase = new ClearSingleLane(previousState, Direction.EAST);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.RED, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.GREEN, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldClearNorthWhenOnlyNorthIsGreen() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.GREEN,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.RED
        ));

        // when
        ClearSingleLane phase = new ClearSingleLane(previousState, Direction.NORTH);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.RED, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldClearWestWhenOnlyWestIsGreen() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(
                Direction.NORTH, LightColor.RED,
                Direction.SOUTH, LightColor.RED,
                Direction.EAST, LightColor.RED,
                Direction.WEST, LightColor.GREEN
        ));

        // when
        ClearSingleLane phase = new ClearSingleLane(previousState, Direction.WEST);
        Map<Direction, LightColor> result = phase.getLightColors();

        // then
        assertAll(
                () -> assertEquals(LightColor.RED, result.get(Direction.NORTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.SOUTH)),
                () -> assertEquals(LightColor.RED, result.get(Direction.EAST)),
                () -> assertEquals(LightColor.YELLOW, result.get(Direction.WEST))
        );
    }

    @Test
    void shouldNotMoveWhenLightWasChanged() {
        // given
        when(previousState.getLightColors()).thenReturn(Map.of(Direction.NORTH, LightColor.GREEN, Direction.SOUTH, LightColor.GREEN));
        ClearSingleLane state = new ClearSingleLane(previousState, Direction.NORTH);

        // then
        assertFalse(state.canMove(null, Direction.NORTH));
    }

    @Test
    void shouldNotMoveWhenLeftWhenOppositeLightIsYellow() {
        // given
        LanesGroup lanesGroup = mock(LanesGroup.class);
        Lane currentLane = mock(Lane.class);
        Lane oppositeLane = mock(Lane.class);

        when(previousState.getLightColors()).thenReturn(Map.of(Direction.NORTH, LightColor.GREEN, Direction.SOUTH, LightColor.GREEN));
        when(lanesGroup.getLane(Direction.SOUTH)).thenReturn(currentLane);
        when(currentLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.LEFT));

        when(lanesGroup.getLane(Direction.NORTH)).thenReturn(oppositeLane);
        when(oppositeLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.RIGHT));

        ClearSingleLane state = new ClearSingleLane(previousState, Direction.NORTH);

        // then
        assertFalse(state.canMove(lanesGroup, Direction.SOUTH));
    }

    @Test
    void shouldMoveWhenLightWasNotChanged() {
        // given
        LanesGroup lanesGroup = mock(LanesGroup.class);
        Lane currentLane = mock(Lane.class);

        when(previousState.getLightColors()).thenReturn(Map.of(Direction.NORTH, LightColor.GREEN, Direction.SOUTH, LightColor.GREEN));
        when(lanesGroup.getLane(Direction.SOUTH)).thenReturn(currentLane);
        when(currentLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.STRAIGHT));

        ClearSingleLane state = new ClearSingleLane(previousState, Direction.NORTH);

        // then
        assertTrue(state.canMove(lanesGroup, Direction.SOUTH));
    }
}