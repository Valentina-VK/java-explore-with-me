package ru.practicum.ewm.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private boolean pinned;
    @Size(max = 50, min = 1, message = "максимальная длина поля title — 50 символов, минимальная длина — 1 символ")
    private String title;
}