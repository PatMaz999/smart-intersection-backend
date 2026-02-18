package org.smartintersection.domain.model.vehicle;

import lombok.Getter;

@Getter
public class Car implements Vehicle {
    private VehicleId id;
    private Direction direction;
}
