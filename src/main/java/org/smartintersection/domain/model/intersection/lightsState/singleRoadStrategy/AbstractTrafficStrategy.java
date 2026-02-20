package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.TrafficStrategy;

//FIXME: to remove
public abstract class AbstractTrafficStrategy implements TrafficStrategy {
    @Getter
    private LanesGroup lanes;

}
