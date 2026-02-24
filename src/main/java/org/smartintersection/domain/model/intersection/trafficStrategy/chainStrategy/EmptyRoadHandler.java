package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

public class EmptyRoadHandler extends AbstractTrafficHandler {

    @Override
    public ScheduledState handle(LanesGroup lanes, LightsState currentState) {
        if (lanes.getTotalVehicles() != 0)
            return handleNext(lanes, currentState);

        if (currentState.isOptimal()) {
            return new ScheduledState(currentState, 1);
        } else {
            return wrapLine(getInitialState(Direction.NORTH),1);
        }
    }

    private LightsState getInitialState(Direction direction) {
        return new StraightLineGreen(direction);
    }
}
