package org.smartintersection.application.command;

import org.smartintersection.domain.model.intersection.Intersection;

public interface Command {
    void execute(Intersection intersection);
}
