package org.smartintersection.domain.model.intersection.lightsState;

public interface TrafficStrategy {
    boolean shouldChange();
    LightsState changeState();
    LightsState getInitialState();
}
