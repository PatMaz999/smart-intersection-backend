package org.smartintersection.infrastructure.dto.output;

import java.util.List;

public record SimulationOutput(
        List<StepStatus> stepStatuses
) {
}
