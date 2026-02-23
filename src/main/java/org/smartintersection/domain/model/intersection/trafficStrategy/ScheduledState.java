package org.smartintersection.domain.model.intersection.trafficStrategy;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public class ScheduledState{

    @Getter
    private LightsState state;
    @Getter
    private int timeLeft;

    public ScheduledState(LightsState state, int timeLeft) {
        this.state = state;
        this.timeLeft = timeLeft;
    }

    public LightsState returnState() {
        timeLeft--;
        return state;
    }
}
