package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.Map;
import java.util.Optional;

public class StraightLineGreen extends AbstractLightsState {


    public StraightLineGreen(Direction direction) {
        super(createMap(direction));
    }

    private static Map<Direction, LightColor> createMap(Direction direction) {
        Map<Direction, LightColor> map;
        if(direction == Direction.NORTH || direction == Direction.SOUTH){
            map = Map.of(
                    Direction.NORTH, LightColor.GREEN,
                    Direction.SOUTH, LightColor.GREEN,
                    Direction.WEST, LightColor.RED,
                    Direction.EAST, LightColor.RED
            );
            return map;
        }
        if(direction == Direction.EAST || direction == Direction.WEST){
            map = Map.of(
                    Direction.NORTH, LightColor.RED,
                    Direction.SOUTH, LightColor.RED,
                    Direction.WEST, LightColor.GREEN,
                    Direction.EAST, LightColor.GREEN
            );
            return map;
        }
        throw new IllegalArgumentException("Invalid direction");
    }

    @Override
    public boolean canMove(LanesGroup lanes, Direction direction) {
        if(getColors().get(direction) != LightColor.GREEN)
            return false;

        Lane currentLane = lanes.getLane(direction);

        Optional<TurnDirection> turnDirection = currentLane.nextCarTurnDirection();

        if(turnDirection.isEmpty())
            return false;

        if(turnDirection.get() != TurnDirection.LEFT)
            return true;

        Lane oppositeLane = lanes.getLane(direction.getOpposite());
        Optional<TurnDirection> oppositeTurnDirection = oppositeLane.nextCarTurnDirection();
        return oppositeTurnDirection.isPresent() && oppositeTurnDirection.get() == TurnDirection.LEFT;
    }
}