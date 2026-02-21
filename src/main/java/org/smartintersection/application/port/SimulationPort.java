package org.smartintersection.application.port;

import org.smartintersection.application.command.Command;
import org.smartintersection.application.command.CommandResult;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.trafficStrategy.TrafficStrategy;

import java.util.List;

public interface SimulationPort {
    List<CommandResult> runSimulation(
            LanesGroup lanesConfiguration,
            TrafficStrategy trafficStrategy,
            List<Command> commands
    );
}
