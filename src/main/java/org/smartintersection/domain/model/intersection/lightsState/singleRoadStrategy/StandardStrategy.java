package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Intersection;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lanes.StandardLanes;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.List;
import java.util.Map;
import java.util.Queue;

public class StandardStrategy extends AbstractTrafficStrategy {

//    private static final int REQUIRED_PRIORITY = 6;

    private Intersection intersection;
    private List<LightsState> allowedLightsStates;
    private Queue<ScheduledState> statesQueue;
    private int maxWaitingTime;

    public StandardStrategy(List<LightsState> lightsStates) {
        this.allowedLightsStates = lightsStates;
    }

    public LightsState getInitialState(Direction prioritizeDirection) {
        if(prioritizeDirection == Direction.NORTH ||  prioritizeDirection == Direction.SOUTH)
            return new NorthSouthGreen();
        return new WestEastGreen();
    }

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

    private LightsState findByThroughput(StandardLanes currentLanesCopy, int timeToMaxWaitingTime, LightsState currentState) {
        LightsState bestState = null;
        int maxThroughput = 0;
        for(LightsState lightsState : allowedLightsStates){
            int currentThroughput = calculateThroughput(currentLanesCopy, lightsState, currentState, timeToMaxWaitingTime);
            if(currentThroughput > maxThroughput){
                maxThroughput = currentThroughput;
                bestState = lightsState;
            }
        }
        return bestState;
    }

    private int calculateThroughput(LanesGroup lanes, LightsState lights, LightsState currentState, int ticks){
        StandardLanes currentLanesCopy = lanes.clone();
        int totalThroughput = 0;

        Map<Direction, Boolean> canMove = Map.of(
                Direction.NORTH, false,
                Direction.SOUTH, false,
                Direction.WEST, false,
                Direction.EAST, false
        );

//        calculate throughput for lights change
        if(lights != currentState){
            ticks--;
            var temporaryState = currentState.nextState();
            for(var record : canMove.entrySet()){
                record.setValue(temporaryState.canMove(currentLanesCopy, record.getKey()));
            }

            for(var record : canMove.entrySet()){
                if(record.getValue()){
                    totalThroughput++;
                    currentLanesCopy.getLane(record.getKey()).passNextVehicle();
                }
            }
        }

        for(int i=0; i < ticks; i++){
            for(var record : canMove.entrySet()){
                record.setValue(lights.canMove(currentLanesCopy, record.getKey()));
            }

            for(var record : canMove.entrySet()){
                if(record.getValue()){
                    totalThroughput++;
                    currentLanesCopy.getLane(record.getKey()).passNextVehicle();
                }
            }
        }
        return totalThroughput;
    }

    private LightsState findByPriority(StandardLanes currentLanesCopy){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
