package org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.SingleLaneGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.ScheduledState;

public abstract class AbstractTrafficHandler {

    protected final static int DEFAULT_CARS_PER_PHASE = 12;
    protected final static int MIN_CARS_PER_PHASE = 3;
    protected final static int MAX_CARS_PER_PHASE = 18;

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

    protected int calculateAllowedCars(LanesGroup lanes, Direction... greenDirections) {
        int totalVehicles = lanes.getTotalVehicles();

        if (totalVehicles == 0) {
            return 1;
        }

        int targetVehicles = 0;
        int maxCarsOnLane = 0;
        for (Direction direction : greenDirections) {
            int currentCars = lanes.getLane(direction).getCarsCount();
            targetVehicles += currentCars;

            if (currentCars > maxCarsOnLane)
                maxCarsOnLane = currentCars;
        }

        int calculatedCars = calculateCarsWithProportion(targetVehicles, totalVehicles);

        return Math.min(calculatedCars, maxCarsOnLane);
    }

    protected ScheduledState wrapSingleLane(Direction priorityDirection, int duration) {
        return new ScheduledState(new SingleLaneGreen(priorityDirection), duration);
    }

    protected ScheduledState wrapLine(LightsState newState, int duration) {
        return new ScheduledState(newState, duration);
    }

    private static int calculateCarsWithProportion(double targetVehicles, int totalVehicles) {
        double proportion = targetVehicles / totalVehicles;

        int calculatedCars;
        if (proportion < 0.5) {
            double scale = proportion * 2;
            calculatedCars = Math.max((int) Math.round(DEFAULT_CARS_PER_PHASE * scale), MIN_CARS_PER_PHASE);
        }
        else if (proportion > 0.5) {
            double scale = proportion * 2;
            calculatedCars = Math.min((int) Math.round(DEFAULT_CARS_PER_PHASE * scale), MAX_CARS_PER_PHASE);
        }
        else
            calculatedCars = DEFAULT_CARS_PER_PHASE;
        return calculatedCars;
    }

}
