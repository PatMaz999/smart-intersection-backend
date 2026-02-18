package org.smartintersection.domain.model.intersection.lightsState;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Intersection;

@Getter
public abstract class AbstractLightsState implements LightsState {
    private Intersection intersection;

}
