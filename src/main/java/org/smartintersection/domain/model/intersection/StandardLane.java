package org.smartintersection.domain.model.intersection;

import org.smartintersection.domain.model.vehicle.Vehicle;

import java.util.Queue;

public class StandardLane implements Lane {
    Queue<Vehicle> queue;

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
    public void getLinePriority() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
