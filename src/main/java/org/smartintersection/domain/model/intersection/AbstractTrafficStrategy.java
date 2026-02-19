package org.smartintersection.domain.model.intersection;

import lombok.Getter;

public abstract class AbstractTrafficStrategy implements TrafficStrategy {
    @Getter
    private Intersection intersection;

}
