package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.StandardLanes;

import java.util.Map;

public interface LightsState {
    boolean canMove(StandardLanes lanes, Direction direction);
    LightsState nextState();
    boolean isClearancePhase();
    Map<Direction, LightColor> getLightColors();

//TODO: change to LightColor getColor(Direction direction);
    LightColor getNorthColor();
    LightColor getSouthColor();
    LightColor getEastColor();
    LightColor getWestColor();
}
