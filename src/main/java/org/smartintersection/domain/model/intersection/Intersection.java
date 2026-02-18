package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.intersection.lightsState.LightsState;

public class Intersection {
    private Lane northLane;
    private Lane southLane;
    private Lane westLane;
    private Lane eastLane;

    private LightsState lightsState;
}
