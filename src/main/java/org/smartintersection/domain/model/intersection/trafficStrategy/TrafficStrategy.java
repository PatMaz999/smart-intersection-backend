package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public interface TrafficStrategy {
    LightsState changeLightsState(LanesGroup lanes, LightsState currentState);
    LightsState getInitialState(LanesGroup lanes);
}
