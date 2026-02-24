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
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.SingleLaneGreen;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;
import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriorityHandlerTest {

    private static final int WARNING_WAITING_TIME = 12;
    private static final int MAX_WAITING_TIME = 20;

    @Mock
    private LanesGroup lanes;
    @Mock
    private LightsState currentState;
    @Mock
    private AbstractTrafficHandler nextHandler;
    @Mock
    private Lane priorityLane;
    @Mock
    private Lane oppositeLane;

    @Mock
    private Vehicle collidingCar1;

    @InjectMocks
    private PriorityHandler handler = new PriorityHandler(WARNING_WAITING_TIME, MAX_WAITING_TIME);


    @Test
    void shouldDelegateToNextWhenMaxPriorityIsBelowWarningThreshold() {
//        given
        when(lanes.getMaxPriority()).thenReturn(WARNING_WAITING_TIME - 1);

//        when
        handler.handle(lanes, currentState);

//        then
        verify(nextHandler).handle(lanes, currentState);
    }

    @Test
    void shouldSwitchToStraightLineGreenWhenWaitingStraightOnRed() {
//        given
        when(lanes.getMaxPriority()).thenReturn(WARNING_WAITING_TIME + 1);
        when(lanes.getMaxPriorityDirection()).thenReturn(Direction.NORTH);
        when(currentState.getByColor(LightColor.GREEN)).thenReturn(Set.of(Direction.EAST, Direction.WEST));

        when(lanes.getLane(Direction.NORTH)).thenReturn(priorityLane);
        when(lanes.getLane(Direction.SOUTH)).thenReturn(oppositeLane);

        when(priorityLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.STRAIGHT));

        when(lanes.getTotalVehicles()).thenReturn(10);
        when(priorityLane.getCarsCount()).thenReturn(5);
        when(oppositeLane.getCarsCount()).thenReturn(5);

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(nextHandler, never()).handle(any(), any());
        assertNotNull(result);

        assertInstanceOf(StraightLineGreen.class, result.getState());
        assertTrue(result.getState().getByColor(LightColor.GREEN).contains(Direction.NORTH));
    }

    @Test
    void shouldCreateSingleLaneGreenWhenLeftTurnExceedsMaxWaitingTimeOnGreen() {
//        given
        when(lanes.getMaxPriority()).thenReturn(MAX_WAITING_TIME - 2);
        when(lanes.getMaxPriorityDirection()).thenReturn(Direction.NORTH);

        when(currentState.getByColor(LightColor.GREEN)).thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        when(lanes.getLane(Direction.NORTH)).thenReturn(priorityLane);
        when(lanes.getLane(Direction.SOUTH)).thenReturn(oppositeLane);

        when(priorityLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.LEFT));

        when(collidingCar1.getTurnDirection()).thenReturn(TurnDirection.STRAIGHT);
        when(oppositeLane.getQueue()).thenReturn(List.of(
                collidingCar1, collidingCar1, collidingCar1,
                collidingCar1, collidingCar1, collidingCar1
        ));

        when(lanes.getTotalVehicles()).thenReturn(20);
        when(priorityLane.getCarsCount()).thenReturn(5);

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(nextHandler, never()).handle(any(), any());

        assertInstanceOf(SingleLaneGreen.class, result.getState());
    }

    @Test
    void shouldExtendCurrentStateWhenLeftTurnUnderMaxTimeOnGreen() {
//        given
        when(lanes.getMaxPriority()).thenReturn(MAX_WAITING_TIME - 3);
        when(lanes.getMaxPriorityDirection()).thenReturn(Direction.NORTH);
        when(currentState.getByColor(LightColor.GREEN)).thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        when(lanes.getLane(Direction.NORTH)).thenReturn(priorityLane);
        when(lanes.getLane(Direction.SOUTH)).thenReturn(oppositeLane);
        when(priorityLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.LEFT));

        when(collidingCar1.getTurnDirection()).thenReturn(TurnDirection.STRAIGHT);
        when(oppositeLane.getQueue()).thenReturn(List.of(collidingCar1, collidingCar1));

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(nextHandler, never()).handle(any(), any());

        assertEquals(currentState, result.getState());

        assertEquals(3, result.getTimeLeft());
    }

    @Test
    void shouldDelegateToNextIfGreenAlreadyActiveAndNotTurningLeft() {
//        given
        when(lanes.getMaxPriority()).thenReturn(WARNING_WAITING_TIME + 1);
        when(lanes.getMaxPriorityDirection()).thenReturn(Direction.NORTH);

        when(currentState.getByColor(LightColor.GREEN)).thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        when(lanes.getLane(Direction.NORTH)).thenReturn(priorityLane);
        when(priorityLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.STRAIGHT));

        when(lanes.getLane(Direction.SOUTH)).thenReturn(oppositeLane);
        when(oppositeLane.getQueue()).thenReturn(java.util.Collections.emptyList());

//        when
        handler.handle(lanes, currentState);

//        then
        verify(nextHandler).handle(lanes, currentState);
    }

    @Test
    void shouldNotStarveCarWaitingOnRedWhenItWantsToTurnLeftAndWaitTimeIsAboveWarning() {
//        given
        when(currentState.getByColor(LightColor.GREEN)).thenReturn(Set.of(Direction.NORTH, Direction.SOUTH));

        when(lanes.getMaxPriority()).thenReturn(WARNING_WAITING_TIME + 3);
        when(lanes.getMaxPriorityDirection()).thenReturn(Direction.WEST);

        when(lanes.getLane(Direction.WEST)).thenReturn(priorityLane);
        when(lanes.getLane(Direction.EAST)).thenReturn(oppositeLane);

        when(priorityLane.nextCarTurnDirection()).thenReturn(Optional.of(TurnDirection.LEFT));
        when(oppositeLane.getQueue()).thenReturn(java.util.Collections.emptyList());

        when(lanes.getTotalVehicles()).thenReturn(41);
        when(priorityLane.getCarsCount()).thenReturn(1);
        when(oppositeLane.getCarsCount()).thenReturn(0);

//        when
        ScheduledState result = handler.handle(lanes, currentState);

//        then
        verify(nextHandler, never()).handle(any(), any());
        assertNotNull(result);

        assertInstanceOf(StraightLineGreen.class, result.getState());
        assertTrue(result.getState().getByColor(LightColor.GREEN).contains(Direction.WEST));
    }
}