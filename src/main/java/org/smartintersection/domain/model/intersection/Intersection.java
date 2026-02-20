package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.List;

public interface Intersection {
    void proceed(List<Vehicle> leavingVehicles);
    LightsState getLightsState();
}
