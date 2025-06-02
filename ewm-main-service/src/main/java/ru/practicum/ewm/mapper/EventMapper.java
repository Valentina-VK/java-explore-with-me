package ru.practicum.ewm.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.util.DateTimeMapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = DateTimeMapper.class)
public interface EventMapper {

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "views")
    @Mapping(ignore = true, target = "confirmedRequests")
    @Mapping(source = "cat", target = "category")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    Event toEntity(NewEventDto dto, Category cat, User initiator, EventState state, LocalDateTime createdOn);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "cat", target = "category")
    @Mapping(ignore = true, target = "id")
    Event update(UpdateEventAdminRequest dto, Category cat, @MappingTarget Event oldEvent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(source = "cat", target = "category")
    @Mapping(ignore = true, target = "id")
    Event update(UpdateEventUserRequest dto, Category cat, @MappingTarget Event oldEvent);

    @Mapping(target = "initiator.countSubscribers", source = "initiator.count")
    EventFullDto toDto(Event event);

    @Mapping(target = "initiator.countSubscribers", source = "initiator.count")
    EventShortDto toShortDto(Event event);
}