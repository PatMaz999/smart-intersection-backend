package org.smartintersection.infrastructure.dto.input;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SimulationInputDto(
        @NotEmpty
        @Valid
        List<CommandDto> commands
) {
}
