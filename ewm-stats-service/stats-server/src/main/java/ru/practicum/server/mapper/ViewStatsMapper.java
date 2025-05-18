package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ViewStatsMapper {
    ViewStatsDto toDto(ViewStats stat);

    List<ViewStatsDto> toDto(List<ViewStats> stats);
}
