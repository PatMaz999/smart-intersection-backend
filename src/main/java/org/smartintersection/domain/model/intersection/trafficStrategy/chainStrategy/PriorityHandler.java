package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.Optional;
import java.util.Set;

public class PriorityHandler extends AbstractTrafficHandler {

    private final int warningWaitingTime;
    private final int maxWaitingTime;

    public PriorityHandler() {
        this.warningWaitingTime = 12;
        this.maxWaitingTime = 20;
    }

    public PriorityHandler(int warningWaitingTime, int maxWaitingTime) {
        this.warningWaitingTime = warningWaitingTime;
        this.maxWaitingTime = maxWaitingTime;
    }

    @Override
    public ScheduledState handle(LanesGroup lanes, LightsState currentState) {
        Set<Direction> greenDirections = currentState.getByColor(LightColor.GREEN);
        int currentWaitingTime = lanes.getMaxPriority();

        if (currentWaitingTime > warningWaitingTime) {
            Direction priorityDirection = lanes.getMaxPriorityDirection();
            Optional<TurnDirection> currentTurnDirection = lanes.getLane(priorityDirection).nextCarTurnDirection();

            if (currentTurnDirection.isPresent() && currentTurnDirection.get() == TurnDirection.LEFT) {
                Lane oppositeLane = lanes.getLane(priorityDirection.getOpposite());
                int seriesOfCollidingCars = 0;

                for (var car : oppositeLane.getQueue()) {
                    if (car.getTurnDirection() != TurnDirection.LEFT) {
                        seriesOfCollidingCars++;
                    } else {
                        break;
                    }
                }

                int totalWaitingTime = currentWaitingTime + seriesOfCollidingCars;

                if (totalWaitingTime >= maxWaitingTime) {
                    int duration = calculateAllowedCars(lanes, priorityDirection);
                    if (greenDirections.contains(priorityDirection)) {
                        return wrapSingleLane(priorityDirection, duration);
                    } else {
                        return wrapLine(currentState, duration);
                    }
                }

                if (greenDirections.contains(priorityDirection)) {
                    int duration = Math.min(seriesOfCollidingCars + 1, DEFAULT_CARS_PER_PHASE);
                    return wrapLine(currentState, duration);
                }

            } else if (!greenDirections.contains(priorityDirection)) {
                int duration = calculateAllowedCars(lanes, priorityDirection, priorityDirection.getOpposite());
                return wrapLine(new StraightLineGreen(priorityDirection), duration);
            }
        }

        return handleNext(lanes, currentState);
    }
}
