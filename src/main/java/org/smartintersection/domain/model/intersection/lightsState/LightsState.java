package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.Lane;

public interface LightsState {
    boolean canMove(Lane lane);
    LightsState nextState();
}
