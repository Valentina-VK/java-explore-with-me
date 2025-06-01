package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserShortDto;

@Builder
@Data
public class EventShortDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String annotation;

    private String title;

    private UserShortDto initiator;

    private CategoryDto category;

    private String eventDate;

    private boolean paid;

    private int confirmedRequests;

    private int views;
}
