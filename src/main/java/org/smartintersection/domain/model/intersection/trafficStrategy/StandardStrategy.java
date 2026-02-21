package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.*;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.Queue;
import java.util.Set;

public class StandardStrategy implements TrafficStrategy {

    private Queue<ScheduledState> statesQueue;
    private int warningWaitingTime;
    private int maxWaitingTime;

    @Override
    public LightsState changeLightsState(LanesGroup lanes, LightsState currentState) {
        if (statesQueue.isEmpty())
            refillQueue(lanes, currentState);
        return statesQueue.poll().state();
    }

    @Override
    public LightsState getInitialState(LanesGroup lanes) {
        int verticalCarSum = lanes.getLane(Direction.NORTH).getCarsCount() +
                lanes.getLane(Direction.SOUTH).getCarsCount();
        int horizontalCarSum = lanes.getLane(Direction.WEST).getCarsCount() +
                lanes.getLane(Direction.EAST).getCarsCount();
        return verticalCarSum >= horizontalCarSum ?
                new StraightLineGreen(Direction.NORTH) :
                new StraightLineGreen(Direction.WEST);
    }

    public void refillQueue(LanesGroup lanes, LightsState currentState) {
//        empty
        if (lanes.getTotalVehicles() == 0) {
            if (currentState.isOptimal()) {
                extendCurrentPhase(currentState);
                return;
            } else {
                wrapWithClearancePhase(currentState, getInitialState(lanes));
                return;
            }
        }

//        only green empty
        Set<Direction> greenDirections = currentState.getByColor(LightColor.GREEN);
        int carsOnGreen = 0;
        for (Direction direction : greenDirections) {
            carsOnGreen += lanes.getLane(direction).getCarsCount();
        }
        if (carsOnGreen == 0) {
            Direction newDirection = greenDirections.iterator().next().getRight();
            wrapWithClearancePhase(currentState, new StraightLineGreen(newDirection));
            return;
        }

//        too long waiting time
        int currentWaitingTime = lanes.getMaxPriority();
        if (currentWaitingTime > warningWaitingTime) {
            Direction priorityDirection = lanes.getMaxPriorityDirection();

            Lane oppositeLane = lanes.getLane(priorityDirection.getOpposite());
            int seriesOfCollidingCars = 0;
            for (var x : oppositeLane.getQueue()) {
                if (x.getTurnDirection() != TurnDirection.LEFT)
                    seriesOfCollidingCars++;
                else
                    break;
            }
            int totalWaitingTime = currentWaitingTime + seriesOfCollidingCars;

            if (totalWaitingTime >= maxWaitingTime) {
                swapToOneLine(currentState, priorityDirection);
                return;
            }
            if (greenDirections.contains(priorityDirection)) {
                extendCurrentPhase(currentState);
                return;
            }
            wrapWithClearancePhase(currentState, new StraightLineGreen(priorityDirection));
            return;
        }

        extendCurrentPhase(currentState);
//        return;
    }

    private void swapToOneLine(LightsState currentState, Direction priorityDirection) {
        statesQueue.add(new ScheduledState(new ClearSingleLine(currentState, priorityDirection.getOpposite()), 1));
        statesQueue.add(new ScheduledState(new SingleLaneGreen(priorityDirection), 5));
    }

    private void wrapWithClearancePhase(LightsState currentState, LightsState newState) {
        statesQueue.add(new ScheduledState(new ClearancePhase(currentState), 1));
        statesQueue.add(new ScheduledState(newState, 5));
    }

    private void extendCurrentPhase(LightsState currentState) {
        statesQueue.add(new ScheduledState(currentState, 5));
    }

    //    TODO: to remove
    private boolean requiresDedicatedLeftTurn(LanesGroup lanes) {
        for (Lane lane : lanes.getLanes().values()) {
            if (lane.nextCarTurnDirection() != TurnDirection.LEFT)
                continue;
            int steps = 0;
            int series = 0;
            for (var qu : lane.getQueue()) {
                if (qu.getTurnDirection() == TurnDirection.LEFT)
                    series++;
                steps++;
                if (steps >= 5)
                    break;
            }
            if (series >= 3)
                return true;
        }
        return false;
    }
}
