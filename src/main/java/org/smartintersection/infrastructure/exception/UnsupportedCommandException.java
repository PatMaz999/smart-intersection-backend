package org.smartintersection.infrastructure.exception;

public class UnsupportedCommandException extends RuntimeException {
    public UnsupportedCommandException(Class<?> commandClass) {
        super("Unsupported command type: " + commandClass.getSimpleName());
    }
}
