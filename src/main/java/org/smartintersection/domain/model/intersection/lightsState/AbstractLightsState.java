package org.smartintersection.domain.model.intersection.lightsState;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Intersection;

import java.util.Map;

@Getter
public abstract class AbstractLightsState implements LightsState {
    private final LightColor northLane;
    private final LightColor southLane;
    private final LightColor westLane;
    private final LightColor eastLane;

    private Intersection intersection;

    public AbstractLightsState(LightColor northLane, LightColor southLane, LightColor westLane, LightColor eastLane) {
        this.northLane = northLane;
        this.southLane = southLane;
        this.westLane = westLane;
        this.eastLane = eastLane;
    }

    @Override
    public boolean isClearancePhase() {
        return !(northLane == LightColor.GREEN ||
                southLane == LightColor.GREEN ||
                westLane == LightColor.GREEN ||
                eastLane == LightColor.GREEN);
    }

    @Override
    public Map<Direction, LightColor> getLightColors() {
        return Map.of(
                Direction.NORTH, getNorthLane(),
                Direction.SOUTH, getSouthLane(),
                Direction.EAST, getEastLane(),
                Direction.WEST, getWestLane()
        );
    }

    @Override
    public LightColor getNorthColor() {
        return northLane;
    }

    @Override
    public LightColor getSouthColor() {
        return southLane;
    }

    @Override
    public LightColor getEastColor() {
        return eastLane;
    }

    @Override
    public LightColor getWestColor() {
        return westLane;
    }
}
