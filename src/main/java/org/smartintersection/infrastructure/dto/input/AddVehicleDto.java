package org.smartintersection.infrastructure.dto.input;

public record AddVehicleDto(
//        TODO: add direction name validation
        String vehicleId,
        String startRoad,
        String endRoad
) implements CommandDto {
}
