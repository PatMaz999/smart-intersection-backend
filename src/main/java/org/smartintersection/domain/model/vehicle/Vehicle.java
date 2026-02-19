package org.smartintersection.domain.model.vehicle;

public interface Vehicle {
    VehicleId getId();
    TurnDirection getTurnDirection();
    int getPriority();
    void incrementPriority();
}
