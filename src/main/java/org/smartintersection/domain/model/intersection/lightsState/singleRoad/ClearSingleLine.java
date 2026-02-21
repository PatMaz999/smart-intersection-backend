package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;
import org.smartintersection.domain.model.intersection.lightsState.LightsState;

import java.util.HashMap;
import java.util.Map;

public class ClearSingleLine extends AbstractLightsState {

    public ClearSingleLine(LightsState lightsState, Direction direction) {
        super(removeGreen(lightsState.getLightColors(), direction));
    }

    private static Map<Direction, LightColor> removeGreen(Map<Direction, LightColor> colors, Direction direction) {
        Map<Direction, LightColor> map = new HashMap<>(colors);
        map.replace(direction, LightColor.RED);
        return map;
    }

    @Override
    public boolean canMove(LanesGroup lanes, Direction direction) {
        return false;
    }
}
