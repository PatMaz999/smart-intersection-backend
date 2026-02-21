package org.smartintersection.application.command;

import org.smartintersection.domain.model.intersection.Intersection;

import java.util.Optional;

public class Step implements Command {
    @Override
    public Optional<CommandResult> execute(Intersection intersection) {
        return Optional.of(new LeavingVehicles(intersection.proceed()));
    }
}
