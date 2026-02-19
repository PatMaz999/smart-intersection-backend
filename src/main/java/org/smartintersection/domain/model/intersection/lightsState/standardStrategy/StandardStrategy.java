package org.smartintersection.domain.model.intersection.lightsState.standardStrategy;

import org.smartintersection.domain.model.intersection.AbstractTrafficStrategy;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.LanesConfiguration;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.List;
import java.util.Map;

public class StandardStrategy extends AbstractTrafficStrategy {

//    private static final int REQUIRED_PRIORITY = 6;

    private List<LightsState> allowedLightsStates;
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
    public LightsState getInitialState() {
        int verticalCarSum = getLanes().getNorthLane().getCarsCount() +
                getLanes().getSouthLane().getCarsCount();
        int horizontalCarSum = getLanes().getWestLane().getCarsCount() +
                getLanes().getEastLane().getCarsCount();
        return verticalCarSum >= horizontalCarSum ? new NorthSouthGreen() : new WestEastGreen();
    }

    @Override
    public LightsState findBestState(LightsState currentState) {
        int timeToMaxWaitingTime = maxWaitingTime - getLanes().getMaxPriority();
        LanesConfiguration currentLanesCopy = getLanes().clone(timeToMaxWaitingTime);

        if(timeToMaxWaitingTime > 0){
            return findByThroughput(currentLanesCopy, timeToMaxWaitingTime, currentState);
        }
        else{
            return findByPriority(currentLanesCopy);
        }
    }

    private LightsState findByThroughput(LanesConfiguration currentLanesCopy, int timeToMaxWaitingTime, LightsState currentState) {
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

    private int calculateThroughput(LanesConfiguration lanes, LightsState lights, LightsState currentState, int ticks){
        LanesConfiguration currentLanesCopy = lanes.clone();
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
                    currentLanesCopy.getLaneByDirection(record.getKey()).passNextVehicle();
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
                    currentLanesCopy.getLaneByDirection(record.getKey()).passNextVehicle();
                }
            }
        }
        return totalThroughput;
    }

    private LightsState findByPriority(LanesConfiguration currentLanesCopy){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
