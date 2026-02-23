package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.smartintersection.domain.model.intersection.Direction;
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
        if (newLightState instanceof SingleLaneGreen && currentLightState instanceof StraightLineGreen) {
            Direction newGreenDirection = newLightState.getByColor(LightColor.GREEN).iterator().next();

            if (currentLightState.getLightColors().get(newGreenDirection) == LightColor.GREEN)
                return new ScheduledState(new ClearSingleLane(currentLightState, newGreenDirection.getOpposite()), 1);
        }
        return new ScheduledState(new ClearancePhase(newLightState), 1);
    }
}
