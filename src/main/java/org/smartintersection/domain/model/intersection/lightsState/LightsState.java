package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;
import org.smartintersection.domain.model.intersection.LanesConfiguration;

import java.util.Map;

public interface LightsState {
    boolean canMove(LanesConfiguration lanes, Direction direction);
    LightsState nextState();
    boolean isClearancePhase();
    Map<Direction, LightColor> getLightColors();
    LightColor getNorthColor();
    LightColor getSouthColor();
    LightColor getEastColor();
    LightColor getWestColor();
}
