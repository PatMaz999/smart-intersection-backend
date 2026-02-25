package org.smartintersection.domain.exception;

import org.smartintersection.domain.model.intersection.Direction;

import java.util.Arrays;
import java.util.stream.Collectors;

public class InvalidDirectionException extends RuntimeException {
    public InvalidDirectionException(String invalidValue) {
        super("Unknown direction: '" + invalidValue + "'. Allowed values are: " + getAvailableDirections());
    }

    private static String getAvailableDirections() {
        return Arrays.stream(Direction.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

}
