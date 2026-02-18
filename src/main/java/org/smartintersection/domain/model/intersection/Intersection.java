package org.smartintersection.domain.model.intersection;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

@Getter
public class Intersection {
    private Lane northLane;
    private Lane southLane;
    private Lane westLane;
    private Lane eastLane;

    private LightsState lightsState;
}
