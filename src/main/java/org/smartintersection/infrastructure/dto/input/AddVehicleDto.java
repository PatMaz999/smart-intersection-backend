package org.smartintersection.infrastructure.dto.input;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AddVehicleDto(
        @NotBlank
        @Length(max = 50)
        String vehicleId,
        @NotBlank
        @Length(max = 5)
        String startRoad,
        @NotBlank
        @Length(max = 5)
        String endRoad
) implements CommandDto {
}
