package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.SingleLaneGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

public abstract class AbstractTrafficHandler {

    private AbstractTrafficHandler next;

    public abstract ScheduledState handle(LanesGroup lanes, LightsState currentState);

    protected ScheduledState handleNext(LanesGroup lanes, LightsState currentState){
        if(next == null)
            return new ScheduledState(currentState,1);
        return next.handle(lanes, currentState);
    }

    public AbstractTrafficHandler setNext(AbstractTrafficHandler nextHandler) {
        this.next = nextHandler;
        return this.next;
    }

    protected ScheduledState wrapSingleLane(Direction priorityDirection, int duration) {
        return new ScheduledState(new SingleLaneGreen(priorityDirection), duration);
    }

    protected ScheduledState wrapLine(LightsState newState, int duration) {
        return new ScheduledState(newState, duration);
    }

}
