package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;

import java.util.Map;

public interface LightsState {
    boolean canMove(LanesGroup lanes, Direction direction);
    boolean isClearancePhase();
    Map<Direction, LightColor> getLightColors();
    LightColor getColor(Direction direction);
}
