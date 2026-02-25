package org.smartintersection.domain.model.vehicle;

public record VehicleId(
        String id
) {
    private static int counter = 1;

    public VehicleId {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID cannot be null or blank");
        }
    }

    public VehicleId() {
        this("Vehicle" + (counter++));
    }
}
