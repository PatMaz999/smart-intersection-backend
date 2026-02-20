package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Intersection;
import org.smartintersection.domain.model.intersection.lanes.StandardLanes;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.List;
import java.util.Queue;

public class StandardStrategy extends AbstractTrafficStrategy {

    private Intersection intersection;
    private List<LightsState> allowedLightsStates;
    private Queue<ScheduledState> statesQueue;
    private int requiredPriority;

//    public StandardStrategy(List<LightsState> lightsStates) {
//        this.allowedLightsStates = lightsStates;
//    }
//
//    public LightsState getInitialState(Direction prioritizeDirection) {
//        if(prioritizeDirection == Direction.NORTH ||  prioritizeDirection == Direction.SOUTH)
//            return new NorthSouthGreen();
//        return new WestEastGreen();
//    }

    @Override
    public boolean shouldChangeLights() {
        return statesQueue.peek().timeLeft() <= 0;
    }

    @Override
    public LightsState changeLightsState() {
        if(statesQueue.isEmpty())
//            TODO:
            refillQueue(intersection.getLightsState());
        return statesQueue.poll().state();
    }

    @Override
    public LightsState getInitialState() {
        int verticalCarSum = getLanes().getLane(Direction.NORTH).getCarsCount() +
                getLanes().getLane(Direction.SOUTH).getCarsCount();
        int horizontalCarSum = getLanes().getLane(Direction.WEST).getCarsCount() +
                getLanes().getLane(Direction.EAST).getCarsCount();
        return verticalCarSum >= horizontalCarSum ? new NorthSouthGreen() : new WestEastGreen();
    }
    
    public LightsState refillQueue(LightsState currentState) {
        int timeToMaxWaitingTime = maxWaitingTime - getLanes().getMaxPriority();
        StandardLanes currentLanesCopy = getLanes().clone(timeToMaxWaitingTime);

        if(timeToMaxWaitingTime > 0){
            return findByThroughput(currentLanesCopy, timeToMaxWaitingTime, currentState);
        }
        else{
            return findByPriority(currentLanesCopy);
        }
    }

    private LightsState findByPriority(StandardLanes currentLanesCopy){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
