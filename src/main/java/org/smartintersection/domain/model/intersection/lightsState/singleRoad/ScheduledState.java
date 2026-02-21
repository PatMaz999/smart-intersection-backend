package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public record ScheduledState(
        LightsState state,
        int timeLeft
) {
}
