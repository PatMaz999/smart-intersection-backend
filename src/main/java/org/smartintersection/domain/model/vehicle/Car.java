package org.smartintersection.domain.model.vehicle;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;

@Getter
public class Car implements Vehicle {
    private VehicleId id;
    private TurnDirection turnDirection;
    private int priority;

    public Car(VehicleId id, Direction startDirection, Direction endDirection) {
        this.id = id;
        this.turnDirection = TurnDirection.from(startDirection, endDirection);
        this.priority = 0;
    }

    @Override
    public void incrementPriority() {
        priority++;
    }
}
