package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.vehicle.Vehicle;

public interface Lane {
    void addVehicle(Vehicle vehicle);
    Vehicle passNextVehicle();
    void getLinePriority();
    int getCarsCount();
}
