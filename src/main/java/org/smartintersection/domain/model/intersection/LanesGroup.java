package org.smartintersection.domain.model.intersection;

public interface LanesGroup {
    Lane getLane(Direction direction);
    int getMaxPriority();
    StandardLanes clone(int size);
    StandardLanes clone();
}
