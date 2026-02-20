package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;

import java.util.Map;
import java.util.Set;

public interface LightsState {
    boolean canMove(LanesGroup lanes, Direction direction);
    boolean isClearancePhase();
    Map<Direction, LightColor> getLightColors();
    Set<Direction> getByColor(LightColor color);
    LightColor getColor(Direction direction);
    boolean isOptimal();
}
