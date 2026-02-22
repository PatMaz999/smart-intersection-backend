package org.smartintersection.domain.model.vehicle;

import org.junit.jupiter.api.Test;
import org.smartintersection.domain.model.intersection.Direction;

import static org.junit.jupiter.api.Assertions.*;

class CarTest {

    @Test
    void shouldInitializeWithStartAndEndDirections() {
        // given
        VehicleId id = new VehicleId("TestId");

        // when
        Car car = new Car(id, Direction.NORTH, Direction.EAST);

        // then
        assertAll(
                () -> assertEquals(id, car.getId()),
                () -> assertEquals(TurnDirection.LEFT, car.getTurnDirection()),
                () -> assertEquals(0, car.getPriority())
        );
    }

    @Test
    void shouldInitializeWithTurnDirection() {
        // given
        TurnDirection expectedTurn = TurnDirection.LEFT;

        // when
        Car car = new Car(expectedTurn);

        // then
        assertAll(
                () -> assertNotNull(car.getId()),
                () -> assertEquals(expectedTurn, car.getTurnDirection()),
                () -> assertEquals(0, car.getPriority())
        );
    }

    @Test
    void shouldIncrementPriorityCorrectly() {
        // given
        Car car = new Car(TurnDirection.STRAIGHT);

        // when & then
        assertEquals(0, car.getPriority());

        car.incrementPriority();
        assertEquals(1, car.getPriority());

        car.incrementPriority();
        assertEquals(2, car.getPriority());
    }
}