package org.smartintersection.domain.model.intersection.trafficStrategy;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;

//FIXME: to remove
public abstract class AbstractTrafficStrategy implements TrafficStrategy {
    @Getter
    private LanesGroup lanes;

}
