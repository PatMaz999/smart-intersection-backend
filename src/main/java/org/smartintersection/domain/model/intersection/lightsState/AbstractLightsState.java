package org.smartintersection.domain.model.intersection.lightsState;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;

import java.util.Map;

@Getter
public abstract class AbstractLightsState implements LightsState {

    Map<Direction, LightColor> colors;

    public AbstractLightsState(Map<Direction, LightColor> colors) {
        this.colors = colors;
    }

    @Override
    public boolean isClearancePhase() {
        return !colors.containsValue(LightColor.GREEN);
    }

    @Override
    public Map<Direction, LightColor> getLightColors() {
        return colors;
    }

    @Override
    public LightColor getColor(Direction direction){
        return colors.get(direction);
    }
}
