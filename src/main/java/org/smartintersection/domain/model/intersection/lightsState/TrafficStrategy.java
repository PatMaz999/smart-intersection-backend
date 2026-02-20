package org.smartintersection.domain.model.intersection.lightsState;

import org.smartintersection.domain.model.intersection.lanes.LanesGroup;

public interface TrafficStrategy {
//    boolean shouldChangeLights(LanesGroup lanes);
    LightsState changeLightsState(LanesGroup lanes, LightsState currentState);
    LightsState getInitialState(LanesGroup lanes, LightsState currentState);
}
