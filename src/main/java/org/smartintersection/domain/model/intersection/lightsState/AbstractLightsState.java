package org.smartintersection.domain.model.intersection.lightsState;

import lombok.Getter;
import org.smartintersection.domain.model.intersection.Direction;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Set<Direction> getByColor(LightColor color) {
        return colors.entrySet()
                .stream()
                .filter(light -> light.getValue() == color)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

    }

    @Override
    public LightColor getColor(Direction direction){
        return colors.get(direction);
    }

    @Override
    public boolean isOptimal() {
        return colors.values().stream()
                .filter(l -> l == LightColor.GREEN)
                .count() > 1;
    }
}
