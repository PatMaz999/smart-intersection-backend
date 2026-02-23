package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.ClearSingleLane;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.ClearancePhase;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.SingleLaneGreen;
import org.smartintersection.domain.model.intersection.lightsState.singleRoad.StraightLineGreen;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.AbstractTrafficHandler;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.EmptyGreenHandler;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.EmptyRoadHandler;
import org.smartintersection.domain.model.intersection.trafficStrategy.chainStrategy.PriorityHandler;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.LinkedList;
import java.util.Queue;

public class StandardStrategy implements TrafficStrategy {

    private Queue<ScheduledState> statesQueue;
    private AbstractTrafficHandler trafficHandler;

    public StandardStrategy() {
        this.statesQueue = new LinkedList<>();
        trafficHandler = new EmptyRoadHandler();

        trafficHandler
                .setNext(new EmptyGreenHandler())
                .setNext(new PriorityHandler());
    }

    @Override
    public LightsState changeLightsState(LanesGroup lanes, LightsState currentState) {
        if (statesQueue.isEmpty())
            refillQueue(lanes, currentState);
        return statesQueue.peek().getTimeLeft() <= 1 ? statesQueue.poll().returnState() :
                statesQueue.peek().returnState();
    }

    @Override
    public LightsState getInitialState(LanesGroup lanes) {
        return EmptyRoadHandler.getInitialState(lanes);
    }

    private void refillQueue(LanesGroup lanes, LightsState currentState) {
        ScheduledState nextScheduledState = trafficHandler.handle(lanes, currentState);
        LightsState nextState = nextScheduledState.getState();

        if(!nextState.equals(currentState))
            statesQueue.add(findClearancePhase(currentState, nextState));
        statesQueue.add(nextScheduledState);
    }

    private ScheduledState findClearancePhase(LightsState currentLightState, LightsState newLightState) {
        if (newLightState instanceof SingleLaneGreen && currentLightState instanceof StraightLineGreen && currentLightState.getLightColors().containsKey(newLightState.getByColor(LightColor.GREEN).iterator().next())) {
            Direction newGreenDirection = newLightState.getByColor(LightColor.GREEN).iterator().next();
            return new ScheduledState(new ClearSingleLane(currentLightState, newGreenDirection.getOpposite()), 1);
        }
        return new ScheduledState(new ClearancePhase(newLightState), 1);
    }


//    private void swapToOneLine(LightsState currentState, Direction priorityDirection, int duration) {
//        statesQueue.add(new ScheduledState(new ClearSingleLane(currentState, priorityDirection.getOpposite()), 1));
//        statesQueue.add(new ScheduledState(new SingleLaneGreen(priorityDirection), duration));
//    }
//
//    private void wrapWithClearancePhase(LightsState currentState, LightsState newState, int duration) {
//        statesQueue.add(new ScheduledState(new ClearancePhase(currentState), 1));
//        statesQueue.add(new ScheduledState(newState, duration));
//    }
//
//    private void extendCurrentPhase(LightsState currentState, int duration) {
//        statesQueue.add(new ScheduledState(currentState, duration));
//    }

    //    TODO: to remove
    private boolean requiresDedicatedLeftTurn(LanesGroup lanes) {
        for (Lane lane : lanes.getLanes().values()) {
            var nextTurn = lane.nextCarTurnDirection();
            if (nextTurn.isPresent() && nextTurn.get() != TurnDirection.LEFT)
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
