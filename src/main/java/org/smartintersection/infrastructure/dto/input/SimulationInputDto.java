package org.smartintersection.infrastructure.dto.input;

import java.util.List;

public record SimulationInputDto(
        List<CommandDto> commands
) {
}
