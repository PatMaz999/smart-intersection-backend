package org.smartintersection.domain.model.intersection.lightsState.singleRoadStrategy;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.LanesGroup;
import org.smartintersection.domain.model.intersection.lightsState.AbstractLightsState;
import org.smartintersection.domain.model.intersection.lightsState.LightColor;

import java.util.EnumMap;
import java.util.Map;

public class SingleLaneGreen extends AbstractLightsState {

    public SingleLaneGreen(Direction direction) {
        super(createMap(direction));
    }

    private static Map<Direction, LightColor> createMap(Direction direction) {
        Map<Direction, LightColor> map = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            map.put(dir, (dir == direction) ? LightColor.GREEN : LightColor.RED);
        }
        return map;
    }

    @Override
    public boolean canMove(LanesGroup lanes, Direction direction) {
        return getColors().get(direction) == LightColor.GREEN;
    }
}
