package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.AbstractTrafficHandler;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.EmptyRoadHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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

}