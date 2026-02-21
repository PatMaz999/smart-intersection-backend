package org.smartintersection.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.smartintersection.application.port.SimulationPort;
import org.smartintersection.domain.model.intersection.lanes.StandardLanes;
import org.smartintersection.domain.model.intersection.trafficStrategy.StandardStrategy;
import org.smartintersection.infrastructure.dto.input.SimulationInputDto;
import org.smartintersection.infrastructure.dto.output.SimulationOutput;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonSimulationAdapter {

    private final SimulationPort simulationPort;

    public SimulationOutput runSimpleSimulation(SimulationInputDto inputDto) {
        return mapper.mapToSimulationOutput(
                simulationPort.runSimulation(
                        new StandardLanes(),
                        new StandardStrategy(),
                        mapper.mapToCommand(inputDto)
                )
        );
    }
}
