package org.smartintersection.domain.model.vehicle;

public record VehicleId(
        String id
) {
    private static int counter = 1;

    public VehicleId() {
        this("Vehicle" + (counter++));
    }
}
