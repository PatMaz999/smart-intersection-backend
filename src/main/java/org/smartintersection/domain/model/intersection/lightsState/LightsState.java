package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;

import java.util.Map;

public interface LightsState {
    boolean canMove(Lane lane);
    LightsState nextState();
    boolean isClearancePhase();
    Map<Direction, LightColor> getLightColors();
}
