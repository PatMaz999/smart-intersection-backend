package org.smartintersection.domain.model.vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleIdTest {

    @Test
    void createNoArgsVehicleId() {
        VehicleId vehicleId1 = new VehicleId();
        VehicleId vehicleId2 = new VehicleId();

        assertNotEquals(vehicleId1, vehicleId2);
        assertTrue(vehicleId1.id().matches("Vehicle\\d+"));
        assertTrue(vehicleId2.id().matches("Vehicle\\d+"));
    }

    @Test
    void createCustomVehicleId() {
        String customId = "CustomId";
        VehicleId vehicleId = new VehicleId(customId);

        assertEquals(customId, vehicleId.id());
    }

}