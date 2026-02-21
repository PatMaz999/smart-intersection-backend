package org.smartintersection.application.command;

import org.smartintersection.domain.model.intersection.Intersection;

public class Step implements Command {
    @Override
    public void execute(Intersection intersection) {
        intersection.proceed();
    }
}
