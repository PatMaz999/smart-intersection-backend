package org.smartintersection.domain.model.vehicle;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.exception.IllegalTurnException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TurnDirectionTest {

    @ParameterizedTest
    @EnumSource(Direction.class)
    void shouldThrowWhenStartDirectionEqualsEndDirection(Direction direction) {
//        when
        IllegalTurnException exception = assertThrows(
                IllegalTurnException.class,
                () -> TurnDirection.from(direction, direction)
        );

//        then
        assertTrue(exception.getMessage().contains(direction.toString()));
    }

}