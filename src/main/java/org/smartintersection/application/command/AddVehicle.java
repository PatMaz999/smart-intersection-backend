package org.smartintersection.application.command;

import lombok.Builder;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Intersection;
import org.smartintersection.domain.model.vehicle.Car;
import org.smartintersection.domain.model.vehicle.VehicleId;

import java.util.Optional;

@Builder
public class AddVehicle implements Command {

    VehicleId vehicleId;
    Direction startRoad;
    Direction endRoad;

    @Override
    public Optional<CommandResult> execute(Intersection intersection) {
        intersection.addVehicle(new Car(vehicleId, startRoad, endRoad), startRoad);
        return Optional.empty();
    }
}
