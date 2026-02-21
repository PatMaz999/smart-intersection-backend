package org.smartintersection.application.command;

import org.smartintersection.domain.model.intersection.Intersection;

import java.util.Optional;

public interface Command {
    Optional<CommandResult> execute(Intersection intersection);
}
