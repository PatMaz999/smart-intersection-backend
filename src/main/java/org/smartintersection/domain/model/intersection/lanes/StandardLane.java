package org.smartintersection.domain.model.intersection.lanes;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.vehicle.TurnDirection;
import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class StandardLane implements Lane {
    private Queue<Vehicle> queue;
    private final Direction direction;

    public StandardLane(Direction direction) {
        this.queue = new LinkedList<>();
        this.direction = direction;
    }

    private StandardLane(Queue<Vehicle> queue, int queueSize, Direction direction) {
        this.queue = queue.stream()
                .limit(queueSize)
                .collect(Collectors.toCollection(LinkedList::new));
        this.direction = direction;
    }

    private StandardLane(Queue<Vehicle> queue, Direction direction) {
        this.queue = new LinkedList<>(queue);
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
    public int getPriority() {
        return queue.isEmpty() ? 0 : queue.peek().getPriority();
    }

    @Override
    public void incrementPriority() {
        for(Vehicle vehicle : queue)
            vehicle.incrementPriority();
    }

    @Override
    public TurnDirection nextCarTurnDirection() {
        return queue.isEmpty() ? null : queue.peek().getTurnDirection();
    }

    @Override
    public Collection<Vehicle> getQueue(){
        return Collections.unmodifiableCollection(queue);
    }

    @Override
    public Lane clone(int size){
        return new StandardLane(queue, size, direction);
    }

    @Override
    public Lane clone() {
        return new StandardLane(queue, direction);
    }
}
