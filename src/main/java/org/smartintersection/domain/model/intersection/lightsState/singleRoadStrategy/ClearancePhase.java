package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;

import java.util.HashMap;
import java.util.Map;

public class ClearancePhase extends AbstractLightsState {

    ClearancePhase(AbstractLightsState lightsState) {
        super(removeGreen(lightsState.getLightColors()));
    }

    private static Map<Direction, LightColor> removeGreen(Map<Direction, LightColor> colors) {
        Map<Direction, LightColor> map = new HashMap<>();
        for(var s : colors.entrySet()){
            map.put(s.getKey(), s.getValue() == LightColor.GREEN ? LightColor.YELLOW : LightColor.RED);
        }
        return map;
    }

    @Override
    public boolean canMove(LanesGroup lanes, Direction direction) {
        return false;
    }
}
