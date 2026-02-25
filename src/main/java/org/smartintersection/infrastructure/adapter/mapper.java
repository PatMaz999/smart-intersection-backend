package org.smartintersection.infrastructure.adapter;

import org.smartintersection.application.command.*;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.VehicleId;
import org.smartintersection.infrastructure.dto.input.AddVehicleDto;
import org.smartintersection.infrastructure.dto.input.CommandDto;
import org.smartintersection.infrastructure.dto.input.SimulationInputDto;
import org.smartintersection.infrastructure.dto.input.StepCommandDto;
import org.smartintersection.infrastructure.dto.output.SimulationOutput;
import org.smartintersection.infrastructure.dto.output.StepStatus;
import org.smartintersection.infrastructure.exception.UnsupportedCommandException;

import java.util.List;

public class mapper {

    private mapper(){}

    public static List<Command> mapToCommand(SimulationInputDto inputDto) {
        return inputDto.commands().stream()
                .map(mapper::mapToCommand)
                .toList();
    }

    private static Command mapToCommand(CommandDto commandDto) {
        return switch (commandDto) {
            case null -> throw new IllegalArgumentException("CommandDto cannot be null");
            case AddVehicleDto vehicleDto -> AddVehicle.builder()
                    .vehicleId(new VehicleId(vehicleDto.vehicleId()))
                    .startRoad(Direction.fromString(vehicleDto.startRoad()))
                    .endRoad(Direction.fromString(vehicleDto.endRoad()))
                    .build();
            case StepCommandDto stepCommandDto -> new Step();
            default -> throw new UnsupportedCommandException(commandDto.getClass());
        };

    }


    public static SimulationOutput mapToSimulationOutput(List<CommandResult> commandResults) {
        List<StepStatus> statuses = commandResults.stream()
                .filter(x ->x instanceof LeavingVehicles)
                .map(LeavingVehicles.class::cast)
                .map(v -> {
            List<String> vehiclesIds = v.vehicles().stream()
                    .map(vehicle -> vehicle.getId().id())
                    .toList();
                    return new StepStatus(vehiclesIds);
        })
                .toList();
        return new SimulationOutput(statuses);
    }
}
