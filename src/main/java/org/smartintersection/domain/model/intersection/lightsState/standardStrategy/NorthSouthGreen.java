package org.smartintersection.domain.model.intersection.lightsState.standardStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;


public class NorthSouthGreen extends AbstractLightsState {

    public NorthSouthGreen() {
        super(LightColor.GREEN,
                LightColor.GREEN,
                LightColor.RED_WITH_GREEN_ARROW,
                LightColor.RED_WITH_GREEN_ARROW
        );
    }

    @Override
    public boolean canMove(Lane lane) {

        if (lane.getCarsCount() == 0) {
            return false;
        }

        Direction laneDirection = lane.getDirection();
        TurnDirection carDirection = lane.nextCarTurnDirection();

        if (laneDirection == Direction.NORTH || laneDirection == Direction.SOUTH) {
            if (carDirection == TurnDirection.LEFT) {
                Lane oppositeLane = (laneDirection == Direction.NORTH) ?
                        getIntersection().getSouthLane() : getIntersection().getNorthLane();
                if (oppositeLane.getCarsCount() == 0)
                    return true;
                TurnDirection oppositeTurn = oppositeLane.nextCarTurnDirection();

                return oppositeTurn == TurnDirection.LEFT;
            }
            return true;
        }

        if (laneDirection == Direction.WEST && carDirection == TurnDirection.RIGHT) {
            TurnDirection northTurn = getIntersection().getNorthLane().nextCarTurnDirection();
            return northTurn != TurnDirection.STRAIGHT;
        }

        if (laneDirection == Direction.EAST && carDirection == TurnDirection.RIGHT) {
            TurnDirection southTurn = getIntersection().getSouthLane().nextCarTurnDirection();
            return southTurn != TurnDirection.STRAIGHT;
        }

        return false;
    }

    @Override
    public LightsState nextState() {
        return new NorthSouthYellow();
    }
}
