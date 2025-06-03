package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.enums.StateAction;
import ru.practicum.ewm.model.Location;

@Builder
@Data
public class UpdateEventUserRequest {

    @Size(max = 2000, min = 20, message = "максимальная длина поля annotation — 2000 символов, минимальная длина — 20 символа")
    private String annotation;

    @Size(max = 120, min = 3, message = "максимальная длина поля title — 120 символов, минимальная длина — 3 символа")
    private String title;

    @Size(max = 7000, min = 20, message = "максимальная длина поля description — 7000 символов, минимальная длина — 20 символа")
    private String description;

    @Positive
    private Long category;

    private Location location;

    private String eventDate;

    private Boolean paid;

    private Boolean requestModeration;

    @PositiveOrZero
    private Integer participantLimit;

    private StateAction stateAction;
}