package org.smartintersection.domain.model.intersection.lightsState;

public interface TrafficStrategy {
    LightsState getInitialState();
    LightsState findBestState(LightsState currentState);
}
