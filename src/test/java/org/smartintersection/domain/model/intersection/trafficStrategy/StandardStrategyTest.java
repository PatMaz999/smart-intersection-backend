package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.ClearSingleLane;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.ClearancePhase;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.SingleLaneGreen;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.AbstractTrafficHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardStrategyTest {

    @Mock
    private LanesGroup lanesGroup;

    @Mock
    private LightsState currentState;

    @Mock
    private AbstractTrafficHandler trafficHandler;

    @InjectMocks
    private StandardStrategy standardStrategy;

    @Test
    void shouldNotWrapCurrentState(){
//        given
        ScheduledState scheduledState = new ScheduledState(currentState, 1);

        when(trafficHandler.handle(lanesGroup, currentState)).thenReturn(scheduledState);

//        when
        LightsState greenState = standardStrategy.changeLightsState(lanesGroup, currentState);
        standardStrategy.changeLightsState(lanesGroup, currentState);

//        then
        assertEquals(currentState, greenState);
        verify(trafficHandler, times(2)).handle(lanesGroup, currentState);
    }

    @Test
    void shouldNotCalculateNewLightsStateWhenQueueIsNotEmpty(){
//        given
        ScheduledState scheduledState = new ScheduledState(currentState, 3);

        when(trafficHandler.handle(lanesGroup, currentState)).thenReturn(scheduledState);

//        when
        standardStrategy.changeLightsState(lanesGroup, currentState);
        standardStrategy.changeLightsState(lanesGroup, currentState);
        standardStrategy.changeLightsState(lanesGroup, currentState);

//        then
        verify(trafficHandler, times(1)).handle(lanesGroup, currentState);
    }


    @Test
    void shouldWrapWithFullClearanceWhenChangingStraightLineToOppositeNorth() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.NORTH);
        LightsState newState = new StraightLineGreen(Direction.EAST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingStraightLineToOppositeWest() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.EAST);
        LightsState newState = new StraightLineGreen(Direction.NORTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingFromSingleLaneForNorth() {
        // given
        LightsState currentState = new SingleLaneGreen(Direction.NORTH);
        LightsState newState = new StraightLineGreen(Direction.NORTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingFromSingleLaneForSouth() {
        // given
        LightsState currentState = new SingleLaneGreen(Direction.NORTH);
        LightsState newState = new StraightLineGreen(Direction.SOUTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingFromSingleLaneForWest() {
        // given
        LightsState currentState = new SingleLaneGreen(Direction.WEST);
        LightsState newState = new StraightLineGreen(Direction.WEST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingFromSingleLaneForEast() {
        // given
        LightsState currentState = new SingleLaneGreen(Direction.WEST);
        LightsState newState = new StraightLineGreen(Direction.EAST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }


    @Test
    void shouldWrapWithPartialClearance_WhenChangingStraightToSingleOnSameAxisNorth() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.NORTH);
        LightsState newState = new SingleLaneGreen(Direction.NORTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearSingleLane.class, result);
    }

    @Test
    void shouldWrapWithPartialClearance_WhenChangingStraightToSingleOnSameAxisSouth() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.NORTH);
        LightsState newState = new SingleLaneGreen(Direction.SOUTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearSingleLane.class, result);
    }

    @Test
    void shouldWrapWithPartialClearance_WhenChangingStraightToSingleOnSameAxisWest() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.WEST);
        LightsState newState = new SingleLaneGreen(Direction.WEST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearSingleLane.class, result);
    }

    @Test
    void shouldWrapWithPartialClearance_WhenChangingStraightToSingleOnSameAxisEast() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.WEST);
        LightsState newState = new SingleLaneGreen(Direction.EAST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearSingleLane.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingStraightToSingleOnDifferentAxisEast() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.NORTH);
        LightsState newState = new SingleLaneGreen(Direction.EAST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingStraightToSingleOnDifferentAxisWest() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.NORTH);
        LightsState newState = new SingleLaneGreen(Direction.WEST);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingStraightToSingleOnDifferentAxisNorth() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.EAST);
        LightsState newState = new SingleLaneGreen(Direction.NORTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

    @Test
    void shouldWrapWithFullClearance_WhenChangingStraightToSingleOnDifferentAxisSouth() {
        // given
        LightsState currentState = new StraightLineGreen(Direction.EAST);
        LightsState newState = new SingleLaneGreen(Direction.SOUTH);

        when(trafficHandler.handle(lanesGroup, currentState))
                .thenReturn(new ScheduledState(newState, 5));

        // when
        LightsState result = standardStrategy.changeLightsState(lanesGroup, currentState);

        // then
        assertInstanceOf(ClearancePhase.class, result);
    }

}