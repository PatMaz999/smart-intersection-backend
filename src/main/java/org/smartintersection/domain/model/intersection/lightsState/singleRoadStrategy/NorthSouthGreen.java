package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.StandardLanes;
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
    public boolean canMove(StandardLanes lanes, Direction laneDirection) {

        Lane currentLane = lanes.getLane(laneDirection);

        if (currentLane.getCarsCount() == 0) {
            return false;
        }

        TurnDirection carDirection = currentLane.nextCarTurnDirection();

        if (laneDirection == Direction.NORTH || laneDirection == Direction.SOUTH) {
            if (carDirection == TurnDirection.LEFT) {
                Lane oppositeLane = (laneDirection == Direction.NORTH) ?
                        lanes.getLane(Direction.SOUTH) : lanes.getLane(Direction.NORTH);
                if (oppositeLane.getCarsCount() == 0)
                    return true;
                TurnDirection oppositeTurn = oppositeLane.nextCarTurnDirection();

                return oppositeTurn == TurnDirection.LEFT;
            }
            return true;
        }

        if (laneDirection == Direction.WEST && carDirection == TurnDirection.RIGHT) {
            TurnDirection northTurn = lanes.getLane(Direction.NORTH).nextCarTurnDirection();
            return northTurn != TurnDirection.STRAIGHT;
        }

        if (laneDirection == Direction.EAST && carDirection == TurnDirection.RIGHT) {
            TurnDirection southTurn = lanes.getLane(Direction.SOUTH).nextCarTurnDirection();
            return southTurn != TurnDirection.STRAIGHT;
        }

        return false;
    }

    @Override
    public LightsState nextState() {
        return new NorthSouthYellow();
    }
}
