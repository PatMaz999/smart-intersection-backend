package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

import java.util.Set;

public class EmptyGreenHandler extends AbstractTrafficHandler {

    @Override
    public ScheduledState handle(LanesGroup lanes, LightsState currentState) {
        Set<Direction> greenDirections = currentState.getByColor(LightColor.GREEN);
        int carsOnGreen = 0;
        for (Direction direction : greenDirections) {
            carsOnGreen += lanes.getLane(direction).getCarsCount();
        }
        if (carsOnGreen == 0) {
            Direction newDirection = greenDirections.iterator().next().getRight();
            int duration = calculateAllowedCars(lanes, newDirection, newDirection.getOpposite());
            return wrapLine(new StraightLineGreen(newDirection), duration);
        }
        return handleNext(lanes, currentState);
    }
}