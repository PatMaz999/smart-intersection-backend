package org.smartintersection.domain.model.intersection.lightsState;

public interface LightsState {
    boolean canMove();
    LightsState nextState();
}
