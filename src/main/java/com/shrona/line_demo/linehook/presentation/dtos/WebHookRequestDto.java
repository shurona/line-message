package com.shrona.line_demo.linehook.presentation.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record WebHookRequestDto(
    @JsonProperty("destination") String destination,
    @JsonProperty("events") List<LineEvent> events
) {

}
