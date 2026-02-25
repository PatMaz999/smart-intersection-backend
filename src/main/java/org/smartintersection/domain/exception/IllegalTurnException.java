package org.smartintersection.domain.exception;

import org.smartintersection.domain.model.intersection.Direction;

public class IllegalTurnException extends RuntimeException {
    public IllegalTurnException(Direction startDirection, Direction endDirection) {
        super("Unsupported turn direction " + startDirection + " to " + endDirection);
    }
}
