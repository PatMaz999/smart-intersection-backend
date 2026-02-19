package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Collection;

public interface Lane {
    void addVehicle(Vehicle vehicle);
    Vehicle passNextVehicle();
    int getCarsCount();
    Direction getDirection();
    int getPriority();
    void incrementPriority();
    TurnDirection nextCarTurnDirection();
    Collection<Vehicle> getQueue();
}
