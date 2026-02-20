package org.smartintersection.domain.model.intersection.lanes;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface LanesGroup {
    Map<Direction, Lane> getLanes();
    Lane getLane(Direction direction);
    List<Vehicle> passVehicles(Set<Direction> directions);
    int getMaxPriority();
    StandardLanes clone(int size);
    StandardLanes clone();
}
