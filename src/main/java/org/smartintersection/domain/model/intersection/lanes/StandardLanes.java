package org.smartintersection.domain.model.intersection.lanes;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;

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

    @Override
    public int getMaxPriority(){
        return lanes.values().stream()
                .mapToInt(Lane::getPriority)
                .max()
                .orElse(0);
    }

    @Override
    public void increasePriority(Direction direction) {
        lanes.get(direction).incrementPriority();
    }

    @Override
    public Direction getMaxPriorityDirection(){
        return lanes.entrySet().stream()
                .max(Map.Entry.comparingByValue(Comparator.comparingInt(Lane::getPriority)))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public int getTotalVehicles() {
        return lanes.values().stream()
                .mapToInt(Lane::getCarsCount)
                .sum();
    }

    @Override
    public void addVehicle(Vehicle vehicle, Direction direction) {
        lanes.get(direction).addVehicle(vehicle);
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
}
