package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public interface TrafficStrategy {
    LightsState getInitialState();
    LightsState findBestState(LightsState currentState);
}
