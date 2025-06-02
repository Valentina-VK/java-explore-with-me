package ru.practicum.ewm.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.enums.EventState;
import ru.practicum.ewm.model.Location;

@Builder
@Data
public class EventFullDto {

    private Long id;

    private String annotation;

    private String title;

    private String description;

    private UserShortDto initiator;

    private CategoryDto category;

    private Location location;

    private String eventDate;

    private Boolean paid;

    private Boolean requestModeration;

    private int participantLimit;

    private EventState state;

    private String createdOn;

    private String publishedOn;

    private int confirmedRequests;

    private int views;
}