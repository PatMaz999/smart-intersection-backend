package org.smartintersection.domain.model.intersection.lightsState.standardStrategy;

import org.smartintersection.domain.model.intersection.AbstractTrafficStrategy;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;
import org.smartintersection.domain.model.intersection.TrafficStrategy;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Collection;
import java.util.List;

public class StandardStrategy extends AbstractTrafficStrategy {

    private List<LightsState> allowedLightsStates;

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
    public boolean shouldChangeState() {
//        LightsState currentState = getLanes().getLightsState();
        if(getLanes().getMaxPriority() < 12){
            throw new UnsupportedOperationException("Not supported yet.");
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private LightsState findBestByThroughput(LightsState lights, int ticks){
        var northLane = getLanes().getNorthLane();
        var southLane = getLanes().getSouthLane();
        var westLane = getLanes().getWestLane();
        var eastLane = getLanes().getEastLane();

        for(int i=0; i < ticks; i++){
            if(northLane.hasNext()){
                lights.canMove(northLane.next())
            }
        }
    }

    private LightsState findBestByPriority(int ticks){
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
