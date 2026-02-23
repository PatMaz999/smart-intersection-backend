package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StraightLineGreenTest {

    @Mock
    private LanesGroup lanesGroup;

    @Mock
    private Lane currentLane;

    @Mock
    private Lane oppositeLane;

    private void mockCurrentLane(Direction currentDirection, TurnDirection currentTurn) {
        when(lanesGroup.getLane(currentDirection)).thenReturn(currentLane);
        when(currentLane.nextCarTurnDirection()).thenReturn(Optional.ofNullable(currentTurn));
    }

    private void mockOppositeLane(Direction oppositeDirection, TurnDirection oppositeTurn) {
        when(lanesGroup.getLane(oppositeDirection)).thenReturn(oppositeLane);
        when(oppositeLane.nextCarTurnDirection()).thenReturn(Optional.ofNullable(oppositeTurn));
    }

    private List<AbstractLightsState> getAllStatesExcept(AbstractLightsState exceptState){
        return Stream.of(
                new StraightLineGreen(Direction.NORTH),
                new StraightLineGreen(Direction.WEST),
                new SingleLaneGreen(Direction.NORTH),
                new SingleLaneGreen(Direction.SOUTH),
                new SingleLaneGreen(Direction.EAST),
                new SingleLaneGreen(Direction.WEST)
        ).filter(state -> !state.equals(exceptState))
                .toList();
    }

    @Nested
    @DisplayName("North-South green")
    class NorthSouthGreen {
        private final StraightLineGreen lights = new StraightLineGreen(Direction.NORTH);

        @Test
        void shouldReturnCorrectColorsForNorthSouthAxis() {
            // when
            Map<Direction, LightColor> colors = lights.getColors();

            // then
            assertAll(
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.WEST))
            );
        }

        @Test
        void ShouldNotEqualDifferentStateForVerticalLine(){
//            given
            List<AbstractLightsState> differentStates = getAllStatesExcept(lights);
//            when
            differentStates.forEach(state -> assertNotEquals(state, lights));
        }

        @Test
        void shouldReturnFalseWhenCurrentLaneNorthIsEmpty() {
            // given
            mockCurrentLane(Direction.NORTH, null);

            // when
            boolean result = lights.canMove(lanesGroup, Direction.NORTH);

            // then
            assertFalse(result);
        }

        @Test
        void shouldReturnFalseWhenCurrentLaneSouthIsEmpty() {
            // given
            mockCurrentLane(Direction.SOUTH, null);

            // when
            boolean result = lights.canMove(lanesGroup, Direction.SOUTH);

            // then
            assertFalse(result);
        }

        @Test void northCanGoLeftWhenSouthGoLeft() {
            mockCurrentLane(Direction.NORTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.SOUTH, TurnDirection.LEFT);
            assertTrue(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void northCantGoLeftWhenSouthGoStraight() {
            mockCurrentLane(Direction.NORTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.SOUTH, TurnDirection.STRAIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void northCantGoLeftWhenSouthGoRight() {
            mockCurrentLane(Direction.NORTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.SOUTH, TurnDirection.RIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void northCanGoStraight() {
            mockCurrentLane(Direction.NORTH, TurnDirection.STRAIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void northCanGoRight() {
            mockCurrentLane(Direction.NORTH, TurnDirection.RIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void southCanGoLeftWhenNorthGoLeft() {
            mockCurrentLane(Direction.SOUTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.NORTH, TurnDirection.LEFT);
            assertTrue(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test void southCantGoLeftWhenNorthGoStraight() {
            mockCurrentLane(Direction.SOUTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.NORTH, TurnDirection.STRAIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test void southCantGoLeftWhenNorthGoRight() {
            mockCurrentLane(Direction.SOUTH, TurnDirection.LEFT);
            mockOppositeLane(Direction.NORTH, TurnDirection.RIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test void southCanGoStraight() {
            mockCurrentLane(Direction.SOUTH, TurnDirection.STRAIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test void southCanGoRight() {
            mockCurrentLane(Direction.SOUTH, TurnDirection.RIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test void westCantGoWhenNorthSouthIsGreen() {
            assertFalse(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void eastCantGoWhenNorthSouthIsGreen() {
            assertFalse(lights.canMove(lanesGroup, Direction.EAST));
        }
    }

    @Nested
    @DisplayName("West-East green")
    class EastWestGreen {
        private final StraightLineGreen lights = new StraightLineGreen(Direction.EAST);

        @Test
        void shouldReturnCorrectColorsForEastWestAxis() {
            // when
            Map<Direction, LightColor> colors = lights.getColors();

            // then
            assertAll(
                    () -> assertEquals(LightColor.RED, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.WEST))
            );
        }

        @Test
        void ShouldNotEqualDifferentStateForHorizontalLine(){
//            given
            List<AbstractLightsState> differentStates = getAllStatesExcept(lights);
//            when
            differentStates.forEach(state -> assertNotEquals(state, lights));
        }

        @Test
        void shouldReturnFalseWhenCurrentLaneEastIsEmpty() {
            // given
            mockCurrentLane(Direction.EAST, null);

            // when
            boolean result = lights.canMove(lanesGroup, Direction.EAST);

            // then
            assertFalse(result);
        }

        @Test
        void shouldReturnFalseWhenCurrentLaneWestIsEmpty() {
            // given
            mockCurrentLane(Direction.WEST, null);

            // when
            boolean result = lights.canMove(lanesGroup, Direction.WEST);

            // then
            assertFalse(result);
        }

        @Test void eastCanGoLeftWhenWestGoLeft() {
            mockCurrentLane(Direction.EAST, TurnDirection.LEFT);
            mockOppositeLane(Direction.WEST, TurnDirection.LEFT);
            assertTrue(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test void eastCantGoLeftWhenWestGoStraight() {
            mockCurrentLane(Direction.EAST, TurnDirection.LEFT);
            mockOppositeLane(Direction.WEST, TurnDirection.STRAIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test void eastCantGoLeftWhenWestGoRight() {
            mockCurrentLane(Direction.EAST, TurnDirection.LEFT);
            mockOppositeLane(Direction.WEST, TurnDirection.RIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test void eastCanGoStraight() {
            mockCurrentLane(Direction.EAST, TurnDirection.STRAIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test void eastCanGoRight() {
            mockCurrentLane(Direction.EAST, TurnDirection.RIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test void westCanGoLeftWhenEastGoLeft() {
            mockCurrentLane(Direction.WEST, TurnDirection.LEFT);
            mockOppositeLane(Direction.EAST, TurnDirection.LEFT);
            assertTrue(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void westCantGoLeftWhenEastGoStraight() {
            mockCurrentLane(Direction.WEST, TurnDirection.LEFT);
            mockOppositeLane(Direction.EAST, TurnDirection.STRAIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void westCantGoLeftWhenEastGoRight() {
            mockCurrentLane(Direction.WEST, TurnDirection.LEFT);
            mockOppositeLane(Direction.EAST, TurnDirection.RIGHT);
            assertFalse(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void westCanGoStraight() {
            mockCurrentLane(Direction.WEST, TurnDirection.STRAIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void westCanGoRight() {
            mockCurrentLane(Direction.WEST, TurnDirection.RIGHT);
            assertTrue(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test void northCantGoWhenEastWestIsGreen() {
            assertFalse(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test void southCantGoWhenEastWestIsGreen() {
            assertFalse(lights.canMove(lanesGroup, Direction.SOUTH));
        }
    }
}