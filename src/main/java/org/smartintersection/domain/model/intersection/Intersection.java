package org.smartintersection.domain.model.intersection;

import lombok.Getter;
import lombok.Setter;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.stream.IntStream;

@Getter
public class Intersection {
    private Lane northLane;
    private Lane southLane;
    private Lane westLane;
    private Lane eastLane;

    private LightsState lightsState;

    @Setter
    private TrafficStrategy currentStrategy;

    public int getMaxPriority(){
        return IntStream.of(
                northLane.getPriority(),
                southLane.getPriority(),
                westLane.getPriority(),
                eastLane.getPriority()
        ).max().orElse(0);
    }

}
