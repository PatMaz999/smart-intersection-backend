package org.smartintersection.infrastructure.dto.output;

import java.util.List;

public record StepStatus(
        List<String> leftVehicles
) {
}
