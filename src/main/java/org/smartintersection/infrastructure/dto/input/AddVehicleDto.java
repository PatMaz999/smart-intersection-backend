package org.smartintersection.infrastructure.dto.input;

public record AddVehicleDto(
        String vehicleId,
        String startRoad,
        String endRoad
) implements CommandDto {
}
