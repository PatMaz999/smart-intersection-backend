package org.smartintersection.domain.model.intersection;

import lombok.Getter;

import java.util.stream.IntStream;

@Getter
public class LanesConfiguration {
    private Lane northLane;
    private Lane southLane;
    private Lane westLane;
    private Lane eastLane;

    private LanesConfiguration(Lane northLane, Lane southLane, Lane westLane, Lane eastLane, int size) {
        this.northLane = northLane.clone(size);
        this.southLane = southLane.clone(size);
        this.westLane = westLane.clone(size);
        this.eastLane = eastLane.clone(size);
    }

    private LanesConfiguration(Lane northLane, Lane southLane, Lane westLane, Lane eastLane) {
        this.northLane = northLane.clone();
        this.southLane = southLane.clone();
        this.westLane = westLane.clone();
        this.eastLane = eastLane.clone();
    }

    public int getMaxPriority(){
        return IntStream.of(
                northLane.getPriority(),
                southLane.getPriority(),
                westLane.getPriority(),
                eastLane.getPriority()
        ).max().orElse(0);
    }

    public Lane getLaneByDirection(Direction direction){
        return switch (direction) {
            case NORTH -> northLane;
            case SOUTH -> southLane;
            case WEST  -> westLane;
            case EAST  -> eastLane;
        };
    }

    public LanesConfiguration clone(int size){
        return new LanesConfiguration(northLane, southLane, westLane, eastLane, size);
    }

    public LanesConfiguration clone(){
        return new LanesConfiguration(northLane, southLane, westLane, eastLane);
    }
}
