package org.smartintersection.domain.model.intersection.lanes;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Collection;

//TODO: refactor
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
