package org.smartintersection.infrastructure.dto.input;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AddVehicleDto.class, name = "addVehicle"),
        @JsonSubTypes.Type(value = StepCommandDto.class, name = "step")
})
public interface CommandDto {
}
