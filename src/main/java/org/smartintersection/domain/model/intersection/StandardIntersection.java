package org.smartintersection.domain.model.intersection;

import lombok.Getter;
import lombok.Setter;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.intersection.lightsState.TrafficStrategy;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.List;

@Getter
public class StandardIntersection implements Intersection {

    private LanesGroup standardLanes;

    private LightsState lightsState;

    @Setter
    private TrafficStrategy currentStrategy;

    @Override
    public void proceed(List<Vehicle> leavingVehicles) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
