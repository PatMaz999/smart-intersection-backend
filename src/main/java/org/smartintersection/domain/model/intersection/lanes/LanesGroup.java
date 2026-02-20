package org.smartintersection.domain.model.intersection.lanes;

import org.smartintersection.domain.model.intersection.Direction;

public interface LanesGroup {
    Lane getLane(Direction direction);
    int getMaxPriority();
    StandardLanes clone(int size);
    StandardLanes clone();
}
