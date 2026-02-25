package org.smartintersection.domain.model.intersection.lanes;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Collection;
import java.util.Optional;

public interface Lane {
    void addVehicle(Vehicle vehicle);
    Vehicle passNextVehicle();
    int getCarsCount();
    Direction getDirection();
    int getPriority();
    void incrementPriority();
    Optional<TurnDirection> nextCarTurnDirection();
    Collection<Vehicle> getQueue();
}
