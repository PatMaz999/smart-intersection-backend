package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.AbstractTrafficHandler.MAX_CARS_PER_PHASE;

@ExtendWith(MockitoExtension.class)
class EmptyGreenHandlerTest {

    private static final int DEFAULT_DURATION = 8;

    @Mock
    private LanesGroup lanes;

    @Mock
    private LightsState currentState;

    @Mock
    private AbstractTrafficHandler next;

    @InjectMocks
    private EmptyGreenHandler handler = new EmptyGreenHandler();

    @Test
    void shouldDelegateWhenCarsAreStillOnGreen() {
        // given
        when(currentState.getByColor(LightColor.GREEN))
                .thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        mockNorthSouthLanes(1, 0);

        ScheduledState nextHandlerState = new ScheduledState(mock(LightsState.class), 1);
        when(next.handle(lanes, currentState)).thenReturn(nextHandlerState);

        // when
        ScheduledState result = handler.handle(lanes, currentState);

        // then
        assertEquals(nextHandlerState, result);
        verify(next, times(1)).handle(lanes, currentState);
    }

    @Test
    void shouldSwitchLightsWhenGreenIsEmptyButCarsAreWaitingOnRed() {
//        given
        when(currentState.getByColor(LightColor.GREEN))
                .thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));
        when(lanes.getTotalVehicles()).thenReturn(3);

        mockNorthSouthLanes(0, 0);
        mockEastWestLanes(1, 2);

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(next, never()).handle(any(), any());

        assertNotNull(result);
        assertEquals(2, result.getTimeLeft());

        assertInstanceOf(StraightLineGreen.class, result.getState());
        assertNotEquals(currentState, result.getState());
    }

    @Test
    void shouldCapDurationToMaxCarsPerPhaseWhenTrafficIsExtremelyHeavy() {
//        given
        when(currentState.getByColor(LightColor.GREEN))
                .thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        mockNorthSouthLanes(0, 0);

        int carsOnEast = 30;
        int carsOnWest = 40;
        mockEastWestLanes(carsOnEast, carsOnWest);

        when(lanes.getTotalVehicles()).thenReturn(70);

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(next, never()).handle(any(), any());

        assertNotNull(result);

        assertEquals(MAX_CARS_PER_PHASE, result.getTimeLeft());

        assertInstanceOf(StraightLineGreen.class, result.getState());
        assertNotEquals(currentState, result.getState());
    }

    private void mockNorthSouthLanes(int carsOnNorth, int carsOnSouth) {
        Lane northLane = mock(Lane.class);
        Lane southLane = mock(Lane.class);

        when(lanes.getLane(Direction.NORTH)).thenReturn(northLane);
        when(lanes.getLane(Direction.SOUTH)).thenReturn(southLane);

        when(northLane.getCarsCount()).thenReturn(carsOnNorth);
        when(southLane.getCarsCount()).thenReturn(carsOnSouth);
    }

    private void mockEastWestLanes(int carsOnEast, int carsOnWest) {
        Lane eastLane = mock(Lane.class);
        Lane westLane = mock(Lane.class);

        when(lanes.getLane(Direction.EAST)).thenReturn(eastLane);
        when(lanes.getLane(Direction.WEST)).thenReturn(westLane);

        when(eastLane.getCarsCount()).thenReturn(carsOnEast);
        when(westLane.getCarsCount()).thenReturn(carsOnWest);
    }
}
