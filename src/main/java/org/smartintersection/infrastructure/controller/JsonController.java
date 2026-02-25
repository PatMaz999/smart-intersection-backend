package org.smartintersection.infrastructure.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.smartintersection.infrastructure.adapter.JsonSimulationAdapter;
import org.smartintersection.infrastructure.dto.input.SimulationInputDto;
import org.smartintersection.infrastructure.dto.output.SimulationOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JsonController {

    private final JsonSimulationAdapter jsonSimulationAdapter;

    @PostMapping("/simulate")
    public ResponseEntity<@NonNull SimulationOutput> simulate(@Valid @RequestBody SimulationInputDto inputDto) {
        return ResponseEntity.ok(jsonSimulationAdapter.runSimpleSimulation(inputDto));
    }
}
