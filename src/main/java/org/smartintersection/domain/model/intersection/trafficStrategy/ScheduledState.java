package org.smartintersection.domain.model.intersection.trafficStrategy;

import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public record ScheduledState(
        LightsState state,
        int timeLeft
) {
}
