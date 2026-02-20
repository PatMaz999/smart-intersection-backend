package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.TrafficStrategy;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.Set;

public class StandardStrategy implements TrafficStrategy {

//    private Queue<ScheduledState> statesQueue;
    private int warningWaitingTime;
    private int maxWaitingTime;


//    @Override
//    public boolean shouldChangeLights(LanesGroup lanes) {
//        return requiresDedicatedLeftTurn(lanes)
//                || (!statesQueue.isEmpty() && statesQueue.peek().timeLeft() <= 0);
//    }

    @Override
    public LightsState changeLightsState(LanesGroup lanes, LightsState currentState) {
        return refillQueue(lanes, currentState);
//        if (statesQueue.isEmpty())
////            TODO:
//            refillQueue(intersection.getLightsState());
//        return statesQueue.poll().state();
    }

    @Override
    public LightsState getInitialState(LanesGroup lanes, LightsState currentState) {
        int verticalCarSum = lanes.getLane(Direction.NORTH).getCarsCount() +
                lanes.getLane(Direction.SOUTH).getCarsCount();
        int horizontalCarSum = lanes.getLane(Direction.WEST).getCarsCount() +
                lanes.getLane(Direction.EAST).getCarsCount();
        return verticalCarSum >= horizontalCarSum ?
                new StraightLineGreen(Direction.NORTH) :
                new StraightLineGreen(Direction.WEST);
    }

    //    TODO: add yellow light logic
    public LightsState refillQueue(LanesGroup lanes, LightsState currentState) {
//        empty
        if(lanes.getTotalVehicles() == 0)
            return currentState.isOptimal() ? currentState : getInitialState(lanes, currentState);

//        only green empty
        Set<Direction> greenDirections = currentState.getByColor(LightColor.GREEN);
        int carsOnGreen = 0;
        for(Direction direction : greenDirections) {
            carsOnGreen += lanes.getLane(direction).getCarsCount();
        }
        if(carsOnGreen == 0)
            return new StraightLineGreen(
                    greenDirections
                    .iterator().next()
                    .getRight()
            );

//        too long waiting time
        int currentWaitingTime = lanes.getMaxPriority();
        if(currentWaitingTime > warningWaitingTime){
            Direction priorityDirection = lanes.getMaxPriorityDirection();

            Lane oppositeLane = lanes.getLane(priorityDirection.getOpposite());
            int seriesOfCollidingCars = 0;
            for(var x: oppositeLane.getQueue()){
                if(x.getTurnDirection() != TurnDirection.LEFT)
                    seriesOfCollidingCars++;
                else
                    break;
            }
            int totalWaitingTime = currentWaitingTime + seriesOfCollidingCars;

            if (totalWaitingTime >= maxWaitingTime) {
                return new SingleLaneGreen(priorityDirection);
            }
            if (greenDirections.contains(priorityDirection)) {
                return currentState;
            }
            return new StraightLineGreen(priorityDirection);
        }

        return currentState;
    }

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
