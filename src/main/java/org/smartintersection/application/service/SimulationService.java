package org.smartintersection.application.service;

import org.smartintersection.application.command.Command;
import org.smartintersection.application.command.CommandResult;
import org.smartintersection.application.port.SimulationPort;
import org.smartintersection.domain.model.intersection.Intersection;
import org.smartintersection.domain.model.intersection.StandardIntersection;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.trafficStrategy.TrafficStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SimulationService implements SimulationPort {

    public List<CommandResult> runSimulation(
            LanesGroup lanesConfiguration,
            TrafficStrategy trafficStrategy,
            List<Command> commands
    ){
        Intersection intersection = new StandardIntersection(lanesConfiguration, trafficStrategy);

        return commands.stream()
                .map(command -> command.execute(intersection))
                .flatMap(Optional::stream)
                .toList();
    }
}
