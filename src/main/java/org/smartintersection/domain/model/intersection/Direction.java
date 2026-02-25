package org.smartintersection.domain.model.intersection;

public enum Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public static Direction fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Direction value cannot be null or blank");
        }
        try {
            return Direction.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid direction: " + value);
        }
    }

    public Direction getOpposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
        };
    }

    public Direction getRight() {
        return switch (this) {
            case NORTH -> WEST;
            case EAST  -> NORTH;
            case SOUTH -> EAST;
            case WEST  -> SOUTH;
        };
    }

    public Direction getLeft() {
        return switch (this) {
            case NORTH -> EAST;
            case EAST  -> SOUTH;
            case SOUTH -> WEST;
            case WEST  -> NORTH;
        };
    }
}
