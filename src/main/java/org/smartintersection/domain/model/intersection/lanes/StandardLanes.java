package org.smartintersection.domain.model.intersection.lanes;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
public class StandardLanes implements LanesGroup {
    Map<Direction, Lane> lanes;


    public StandardLanes() {
        this.lanes = Map.of(
                Direction.NORTH, new StandardLane(Direction.NORTH),
                Direction.SOUTH, new StandardLane(Direction.SOUTH),
                Direction.WEST, new StandardLane(Direction.WEST),
                Direction.EAST, new StandardLane(Direction.EAST)
        );
    }

    private StandardLanes(Map<Direction, Lane> lanes, int size) {
        this.lanes = Map.of(
                Direction.NORTH, lanes.get(Direction.NORTH).clone(size),
                Direction.SOUTH, lanes.get(Direction.SOUTH).clone(size),
                Direction.WEST, lanes.get(Direction.WEST).clone(size),
                Direction.EAST, lanes.get(Direction.EAST).clone(size)
        );
    }

    private StandardLanes(Map<Direction, Lane> lanes) {
        this.lanes = Map.of(
                Direction.NORTH, lanes.get(Direction.NORTH).clone(),
                Direction.SOUTH, lanes.get(Direction.SOUTH).clone(),
                Direction.WEST, lanes.get(Direction.WEST).clone(),
                Direction.EAST, lanes.get(Direction.EAST).clone()
        );
    }

    @Override
    public int getMaxPriority(){
        return lanes.values().stream()
                .mapToInt(Lane::getPriority)
                .max()
                .orElse(0);
    }

    public Lane getLane(Direction direction){
        return lanes.get(direction);
    }

    @Override
    public List<Vehicle> passVehicles(Set<Direction> directions) {
        List<Vehicle> vehicles = new ArrayList<>();
        for(Direction direction : directions){
            vehicles.add(lanes.get(direction).passNextVehicle());
        }
        return vehicles;
    }

    @Override
    public StandardLanes clone(int size){
        return new StandardLanes(lanes, size);
    }

    @Override
    public StandardLanes clone(){
        return new StandardLanes(lanes);
    }
}
