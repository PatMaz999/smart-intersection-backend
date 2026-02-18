package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.LinkedList;
import java.util.Queue;

public class StandardLane implements Lane {
    private Queue<Vehicle> queue;
    private final Direction direction;

    public StandardLane(Direction direction) {
        this.queue = new LinkedList<>();
        this.direction = direction;
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        queue.add(vehicle);
    }

    @Override
    public Vehicle passNextVehicle() {
        return queue.poll();
    }

    @Override
    public int getCarsCount() {
        return queue.size();
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public TurnDirection nextCarTurnDirection() {
        return queue.isEmpty() ? null : queue.peek().getTurnDirection();
    }
}
