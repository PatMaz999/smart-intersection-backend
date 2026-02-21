package org.smartintersection.infrastructure.adapter;

import org.smartintersection.application.command.*;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.Vehicle;
import org.smartintersection.domain.model.vehicle.VehicleId;
import org.smartintersection.infrastructure.dto.input.AddVehicleDto;
import org.smartintersection.infrastructure.dto.input.CommandDto;
import org.smartintersection.infrastructure.dto.input.SimulationInputDto;
import org.smartintersection.infrastructure.dto.input.StepCommandDto;
import org.smartintersection.infrastructure.dto.output.SimulationOutput;
import org.smartintersection.infrastructure.dto.output.StepStatus;

import java.util.List;

public class mapper {

    private mapper(){}

    public static List<Command> mapToCommand(SimulationInputDto inputDto) {
        return inputDto.commands().stream()
                .map(mapper::mapToCommand)
                .toList();
    }

    private static Command mapToCommand(CommandDto commandDto) {
        if(commandDto instanceof AddVehicleDto vehicleDto){
            return addVehicle.builder()
                    .vehicleId(new VehicleId(vehicleDto.vehicleId()))
                    .startRoad(mapToDirection(vehicleDto.startRoad()))
                    .endRoad(mapToDirection(vehicleDto.endRoad()))
                    .build();
        }
        else if(commandDto instanceof StepCommandDto){
            return new Step();
        }
        throw new IllegalArgumentException("Invalid Command");
    }

    private static Direction mapToDirection(String direction) {
        try{
        return Direction.valueOf(direction.toUpperCase());
        } catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid Direction");
        }
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
