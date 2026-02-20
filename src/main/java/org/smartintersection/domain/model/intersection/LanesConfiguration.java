package org.smartintersection.domain.model.intersection;

import lombok.Getter;

import java.util.Map;

@Getter
public class LanesConfiguration {
    Map<Direction, Lane> lanes;


    public LanesConfiguration() {
        this.lanes = Map.of(
                Direction.NORTH, new StandardLane(Direction.NORTH),
                Direction.SOUTH, new StandardLane(Direction.SOUTH),
                Direction.WEST, new StandardLane(Direction.WEST),
                Direction.EAST, new StandardLane(Direction.EAST)
        );
    }

    private LanesConfiguration(Map<Direction, Lane> lanes, int size) {
        this.lanes = Map.of(
                Direction.NORTH, lanes.get(Direction.NORTH).clone(size),
                Direction.SOUTH, lanes.get(Direction.SOUTH).clone(size),
                Direction.WEST, lanes.get(Direction.WEST).clone(size),
                Direction.EAST, lanes.get(Direction.EAST).clone(size)
        );
    }

    private LanesConfiguration(Map<Direction, Lane> lanes) {
        this.lanes = Map.of(
                Direction.NORTH, lanes.get(Direction.NORTH).clone(),
                Direction.SOUTH, lanes.get(Direction.SOUTH).clone(),
                Direction.WEST, lanes.get(Direction.WEST).clone(),
                Direction.EAST, lanes.get(Direction.EAST).clone()
        );
    }

    public int getMaxPriority(){
        return lanes.values().stream()
                .mapToInt(Lane::getPriority)
                .max()
                .orElse(0);
    }

    public Lane getLane(Direction direction){
        return lanes.get(direction);
    }

    public LanesConfiguration clone(int size){
        return new LanesConfiguration(lanes, size);
    }

    public LanesConfiguration clone(){
        return new LanesConfiguration(lanes);
    }
}
