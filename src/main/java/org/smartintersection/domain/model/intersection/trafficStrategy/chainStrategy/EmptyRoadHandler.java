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
            return wrapLine(getInitialState(lanes),1);
        }
    }

    public static LightsState getInitialState(LanesGroup lanes) {
        int verticalCarSum = lanes.getLane(Direction.NORTH).getCarsCount() +
                lanes.getLane(Direction.SOUTH).getCarsCount();
        int horizontalCarSum = lanes.getLane(Direction.WEST).getCarsCount() +
                lanes.getLane(Direction.EAST).getCarsCount();
        return verticalCarSum >= horizontalCarSum ?
                new StraightLineGreen(Direction.NORTH) :
                new StraightLineGreen(Direction.WEST);
    }
}
