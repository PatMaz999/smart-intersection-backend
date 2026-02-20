package org.smartintersection.domain.model.intersection;

import lombok.Getter;
import lombok.Setter;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

@Getter
public class Intersection {

    private LanesGroup standardLanes;

    private LightsState lightsState;

    @Setter
    private TrafficStrategy currentStrategy;

}
