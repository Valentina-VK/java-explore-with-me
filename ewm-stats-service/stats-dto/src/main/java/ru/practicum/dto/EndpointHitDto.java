package ru.practicum.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointHitDto {
    @NotNull
    private String app;

    @NotNull
    private String uri;

    @NotNull
    private String ip;

    @NotNull
    private String timestamp;
}
