package org.smartintersection.domain.model.intersection.lightsState.singleRoad;

import org.smartintersection.domain.model.intersection.Direction;
import org.smartintersection.domain.model.intersection.lanes.Lane;
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

        Lane currentLane = lanes.getLane(direction);
        if(currentLane.getCarsCount() == 0 )
            return false;

        return getColors().get(direction) == LightColor.GREEN;
    }
}
