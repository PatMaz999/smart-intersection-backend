package org.smartintersection.domain.model.intersection.lightsState;

public interface TrafficStrategy {
    boolean shouldChangeLights();
    LightsState changeLightsState();
    LightsState getInitialState();
}
