package org.smartintersection.domain.model.intersection.lanes;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.*;

public class StandardLane implements Lane {
    private Queue<Vehicle> queue;
    @Getter
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
    public int getPriority() {
        return queue.isEmpty() ? 0 : queue.peek().getPriority();
    }

    @Override
    public void incrementPriority() {
        for(Vehicle vehicle : queue)
            vehicle.incrementPriority();
    }

    @Override
    public Optional<TurnDirection> nextCarTurnDirection() {
        return Optional.ofNullable(queue.peek())
                .map(Vehicle::getTurnDirection);
    }

    @Override
    public Collection<Vehicle> getQueue(){
        return Collections.unmodifiableCollection(queue);
    }
}
