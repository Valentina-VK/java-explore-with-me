package ru.practicum.ewm.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewCategoryDto {

    @Size(max = 50, min = 1, message = "максимальная длина поля name — 50 символов, минимальная длина — 1 символ")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;
}