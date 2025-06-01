package ru.practicum.ewm.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.Location;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @Size(max = 2000, min = 20, message = "максимальная длина поля annotation — 2000 символов, минимальная длина — 20 символа")
    @NotBlank(message = "Поле name не может быть пустым")
    private String annotation;
    @Size(max = 120, min = 3, message = "максимальная длина поля title — 120 символов, минимальная длина — 3 символа")
    @NotBlank(message = "Поле name не может быть пустым")
    private String title;
    @Size(max = 7000, min = 20, message = "максимальная длина поля description — 7000 символов, минимальная длина — 20 символа")
    @NotBlank(message = "Поле name не может быть пустым")
    private String description;

    @Positive
    @NotNull
    private Long category;

    @NotNull
    private Location location;

    @NotNull
    private String eventDate;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private Boolean requestModeration = true;

    @Builder.Default
    @PositiveOrZero
    private Integer participantLimit = 0;
}