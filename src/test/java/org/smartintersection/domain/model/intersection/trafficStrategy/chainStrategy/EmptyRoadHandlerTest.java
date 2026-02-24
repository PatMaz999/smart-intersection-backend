package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmptyRoadHandlerTest {

    @Mock
    private LanesGroup lanes;

    @Mock
    private LightsState currentState;

    @Mock
    private AbstractTrafficHandler next;

    @InjectMocks
    private EmptyRoadHandler handler = new EmptyRoadHandler();

    @Test
    void shouldNotHandleWhenRoadIsNotEmpty() {
//        given
        ScheduledState newState = new ScheduledState(mock(LightsState.class), 1);

        when(lanes.getTotalVehicles()).thenReturn(1);
        when(next.handle(lanes, currentState)).thenReturn(newState);
//        when
        ScheduledState returnedState = handler.handle(lanes, currentState);
//        then
        verify(next, times(1)).handle(lanes,  currentState);
        assertNotEquals(currentState, newState.getState());
    }

    @Test
    void shouldReturnCurrentStateWhenIsOptimal() {
//        given
        when(lanes.getTotalVehicles()).thenReturn(0);
        when(currentState.isOptimal()).thenReturn(true);
//        when
        ScheduledState returnedState = handler.handle(lanes, currentState);
//        then
        verify(next, never()).handle(any(), any());
        assertEquals(currentState, returnedState.getState());
        assertEquals(1, returnedState.getTimeLeft());
    }

    @Test
    void shouldChangeStateWhenIsNotOptimal() {
//        given
        when(lanes.getTotalVehicles()).thenReturn(0);
        when(currentState.isOptimal()).thenReturn(false);

//        when
        ScheduledState returnedState = handler.handle(lanes, currentState);
//        then
        verify(next, never()).handle(any(), any());
        assertNotEquals(currentState, returnedState.getState());
        assertEquals(1, returnedState.getTimeLeft());
    }

}