package ru.practicum.server.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewStats {
    private String app;

    private String uri;

    private Long hits;
}
