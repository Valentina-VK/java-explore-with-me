package ru.practicum.ewm.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NewUserRequest {

    @Size(max = 250, min = 2, message = "максимальная длина поля name — 250 символов, минимальная длина — 2 символа")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;

    @NotBlank(message = "Поле email не может быть пустым")
    @Size(max = 254, min = 6, message = "максимальная длина поля email — 250 символов, минимальная длина — 2 символа")
    @Email(message = "Поле email не соответствует формату адреса электронной почты")
    private String email;
}