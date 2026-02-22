package org.smartintersection.domain.model.intersection;

import lombok.Getter;
import lombok.Setter;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.trafficStrategy.TrafficStrategy;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class StandardIntersection implements Intersection {

    private LanesGroup standardLanes;

    private LightsState lightsState;

    @Setter
    private TrafficStrategy currentStrategy;

    public StandardIntersection(LanesGroup standardLanes, TrafficStrategy currentStrategy) {
        this.standardLanes = standardLanes;
        this.currentStrategy = currentStrategy;
        this.lightsState = currentStrategy.getInitialState(standardLanes);
    }

    @Override
    public List<Vehicle> proceed() {

        lightsState = currentStrategy.changeLightsState(standardLanes, lightsState);

        Set<Direction> moveSet = new HashSet<>();
        for(Direction direction : Direction.values()){
            if(lightsState.canMove(standardLanes, direction))
                moveSet.add(direction);
            else
                standardLanes.increasePriority(direction);
        }
        return standardLanes.passVehicles(moveSet);
    }

    @Override
    public void addVehicle(Vehicle vehicle, Direction direction) {
        standardLanes.addVehicle(vehicle, direction);
    }
}
