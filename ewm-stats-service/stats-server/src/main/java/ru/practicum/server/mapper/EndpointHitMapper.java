package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

import static ru.practicum.constants.StatsConstants.DATE_TIME_FORMAT;

@Mapper(componentModel = "spring")
public interface EndpointHitMapper {
    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE_TIME_FORMAT)
    @Mapping(ignore = true, target = "id")
    EndpointHit toEntity(EndpointHitDto dto);

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = DATE_TIME_FORMAT)
    EndpointHitDto toDto(EndpointHit hit);
}
