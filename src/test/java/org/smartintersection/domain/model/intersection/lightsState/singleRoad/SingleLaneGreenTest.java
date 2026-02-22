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
import org.smartintersection.domain.model.intersection.lightsState.LightColor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleLaneGreenTest {

    private static final int CAR_ON_LANE = 1;
    private static final int NO_CAR_ON_LANE = 0;

    @Mock
    private LanesGroup lanesGroup;

    @Mock
    private Lane lane;

    @Nested
    @DisplayName("Green for North")
    class NorthGreen {
        private final SingleLaneGreen lights = new SingleLaneGreen(Direction.NORTH);

        @Test
        void shouldReturnCorrectColorsForNorth() {
            Map<Direction, LightColor> colors = lights.getColors();
            assertAll(
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.WEST))
            );
        }

        @Test
        void northShouldMove() {
            mockLane(Direction.NORTH, CAR_ON_LANE);
            assertTrue(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test
        void northShouldNotMoveWhenEmpty() {
            mockLane(Direction.NORTH, NO_CAR_ON_LANE);
            assertFalse(lights.canMove(lanesGroup, Direction.NORTH));
        }

        @Test
        void othersShouldNotMove() {
            mockLane(Direction.SOUTH, CAR_ON_LANE);
            mockLane(Direction.WEST, CAR_ON_LANE);
            mockLane(Direction.EAST, CAR_ON_LANE);
            assertAll(
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.SOUTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.WEST)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.EAST))
            );
        }
    }

    @Nested
    @DisplayName("Green for South")
    class SouthGreen {
        private final SingleLaneGreen lights = new SingleLaneGreen(Direction.SOUTH);

        @Test
        void shouldReturnCorrectColorsForSouth() {
            Map<Direction, LightColor> colors = lights.getColors();
            assertAll(
                    () -> assertEquals(LightColor.RED, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.WEST))
            );
        }

        @Test
        void southShouldMove() {
            mockLane(Direction.SOUTH, CAR_ON_LANE);
            assertTrue(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test
        void southShouldNotMoveWhenEmpty() {
            mockLane(Direction.SOUTH, NO_CAR_ON_LANE);
            assertFalse(lights.canMove(lanesGroup, Direction.SOUTH));
        }

        @Test
        void othersShouldNotMove() {
            mockLane(Direction.NORTH, CAR_ON_LANE);
            mockLane(Direction.WEST, CAR_ON_LANE);
            mockLane(Direction.EAST, CAR_ON_LANE);
            assertAll(
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.NORTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.WEST)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.EAST))
            );
        }
    }

    @Nested
    @DisplayName("Green for East")
    class EastGreen {
        private final SingleLaneGreen lights = new SingleLaneGreen(Direction.EAST);

        @Test
        void shouldReturnCorrectColorsForEast() {
            Map<Direction, LightColor> colors = lights.getColors();
            assertAll(
                    () -> assertEquals(LightColor.RED, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.WEST))
            );
        }

        @Test
        void eastShouldMove() {
            mockLane(Direction.EAST, CAR_ON_LANE);
            assertTrue(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test
        void eastShouldNotMoveWhenEmpty() {
            mockLane(Direction.EAST, NO_CAR_ON_LANE);
            assertFalse(lights.canMove(lanesGroup, Direction.EAST));
        }

        @Test
        void othersShouldNotMove() {
            mockLane(Direction.NORTH, CAR_ON_LANE);
            mockLane(Direction.SOUTH, CAR_ON_LANE);
            mockLane(Direction.WEST, CAR_ON_LANE);
            assertAll(
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.NORTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.SOUTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.WEST))
            );
        }
    }

    @Nested
    @DisplayName("Green for West")
    class WestGreen {
        private final SingleLaneGreen lights = new SingleLaneGreen(Direction.WEST);

        @Test
        void shouldReturnCorrectColorsForWest() {
            Map<Direction, LightColor> colors = lights.getColors();
            assertAll(
                    () -> assertEquals(LightColor.RED, colors.get(Direction.NORTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.SOUTH)),
                    () -> assertEquals(LightColor.RED, colors.get(Direction.EAST)),
                    () -> assertEquals(LightColor.GREEN, colors.get(Direction.WEST))
            );
        }

        @Test
        void westShouldMove() {
            mockLane(Direction.WEST, CAR_ON_LANE);
            assertTrue(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test
        void westShouldNotMoveWhenEmpty() {
            mockLane(Direction.WEST, NO_CAR_ON_LANE);
            assertFalse(lights.canMove(lanesGroup, Direction.WEST));
        }

        @Test
        void othersShouldNotMove() {
            mockLane(Direction.NORTH, CAR_ON_LANE);
            mockLane(Direction.SOUTH, CAR_ON_LANE);
            mockLane(Direction.EAST, CAR_ON_LANE);
            assertAll(
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.NORTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.SOUTH)),
                    () -> assertFalse(lights.canMove(lanesGroup, Direction.EAST))
            );
        }
    }

    private void mockLane(Direction direction, int carsOnLane) {
        when(lanesGroup.getLane(direction)).thenReturn(lane);
        when(lane.getCarsCount()).thenReturn(carsOnLane);
    }
}