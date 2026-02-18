package org.smartintersection.domain.model.intersection.lightsState.standardStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.Lane;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

public class WestEastGreen extends AbstractLightsState {

    public WestEastGreen() {
        super(LightColor.RED_WITH_GREEN_ARROW,
                LightColor.RED_WITH_GREEN_ARROW,
                LightColor.GREEN,
                LightColor.GREEN
        );
    }

    @Override
    public boolean canMove(Lane lane) {

        if (lane.getCarsCount() == 0) {
            return false;
        }

        Direction laneDirection = lane.getDirection();
        TurnDirection carDirection = lane.nextCarTurnDirection();

        if (laneDirection == Direction.WEST || laneDirection == Direction.EAST) {
            if (carDirection == TurnDirection.LEFT) {
                Lane oppositeLane = (laneDirection == Direction.WEST) ?
                        getIntersection().getEastLane() : getIntersection().getWestLane();
                if (oppositeLane.getCarsCount() == 0)
                    return true;
                TurnDirection oppositeTurn = oppositeLane.nextCarTurnDirection();

                return oppositeTurn == TurnDirection.LEFT;
            }
            return true;
        }

        if (laneDirection == Direction.NORTH && carDirection == TurnDirection.RIGHT) {
            TurnDirection eastTurn = getIntersection().getEastLane().nextCarTurnDirection();
            return eastTurn != TurnDirection.STRAIGHT;
        }

        if (laneDirection == Direction.SOUTH && carDirection == TurnDirection.RIGHT) {
            TurnDirection westTurn = getIntersection().getWestLane().nextCarTurnDirection();
            return westTurn != TurnDirection.STRAIGHT;
        }

        return false;
    }

    @Override
    public LightsState nextState() {
        return new WestEastYellow();
    }
}
