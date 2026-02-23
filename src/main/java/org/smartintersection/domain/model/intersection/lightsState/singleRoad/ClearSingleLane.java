package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;
import org.smartintersection.domain.model.vehicle.TurnDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClearSingleLane extends AbstractLightsState {

    public ClearSingleLane(LightsState lightsState, Direction direction) {
        super(removeGreen(lightsState.getLightColors(), direction));
    }

    private static Map<Direction, LightColor> removeGreen(Map<Direction, LightColor> colors, Direction direction) {
        Map<Direction, LightColor> map = new HashMap<>(colors);
        map.replace(direction, LightColor.YELLOW);
        return map;
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

        LightColor oppositeLight = getColors().get(direction.getOpposite());

        if(oppositeLight != LightColor.GREEN && oppositeLight != LightColor.YELLOW)
            return true;

        Lane oppositeLane = lanes.getLane(direction.getOpposite());
        Optional<TurnDirection> oppositeTurnDirection = oppositeLane.nextCarTurnDirection();
        return oppositeTurnDirection.isEmpty() || oppositeTurnDirection.get() == TurnDirection.LEFT;
    }
}
