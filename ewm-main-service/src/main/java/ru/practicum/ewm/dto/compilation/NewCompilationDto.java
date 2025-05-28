package ru.practicum.ewm.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events;
    private boolean pinned;
    @Size(max = 50, min = 1, message = "максимальная длина поля title — 50 символов, минимальная длина — 1 символ")
    @NotBlank(message = "Поле title не может быть пустым")
    private String title;
}