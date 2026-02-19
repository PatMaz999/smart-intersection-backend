package org.smartintersection.domain.model.intersection.lightsState.standardStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;
import org.smartintersection.domain.model.intersection.LanesConfiguration;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

/**
 * allow car turning left to leave the intersection while yellow light is on
 */
public class WestEastYellow extends AbstractLightsState {

    public WestEastYellow() {
        super(LightColor.RED,
                LightColor.RED,
                LightColor.YELLOW,
                LightColor.YELLOW
        );
    }

    @Override
    public boolean canMove(LanesConfiguration lanes, Direction laneDirection) {

        Lane currentLane = lanes.getLaneByDirection(laneDirection);

        if (currentLane.getCarsCount() == 0) {
            return false;
        }

        TurnDirection carDirection = currentLane.nextCarTurnDirection();
        return (laneDirection == Direction.WEST || laneDirection == Direction.EAST)
                && carDirection == TurnDirection.LEFT;
    }

    @Override
    public LightsState nextState() {
        return new NorthSouthGreen();
    }
}
