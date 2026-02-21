package org.smartintersection.application.command;

import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;

public record LeavingVehicles(
        List<Vehicle> vehicles
) implements CommandResult {
    public LeavingVehicles {
        if (vehicles == null)
            vehicles = new ArrayList<>();
    }
}
