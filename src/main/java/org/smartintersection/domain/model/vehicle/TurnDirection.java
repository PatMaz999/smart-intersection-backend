package org.smartintersection.domain.model.vehicle;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.exception.IllegalTurnException;

public enum TurnDirection {
    LEFT, STRAIGHT, RIGHT;

    public static TurnDirection from(Direction start, Direction end) {
        if (start.getOpposite() == end) {
            return STRAIGHT;
        }
        if (start.getRight() == end) {
            return RIGHT;
        }
        if (start.getLeft() == end) {
            return LEFT;
        }

        throw new IllegalTurnException(start, end);
    }
}
