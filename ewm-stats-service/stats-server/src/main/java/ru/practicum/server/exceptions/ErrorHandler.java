package ru.practicum.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidDateTimeException(final NotValidDateTimeException exception) {
        return new ApiError(exception.getMessage(),
                "Нарушение условий для запрашиваемой операции",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ApiError handleServerError(final RuntimeException exception) {
        return new ApiError(exception.getMessage(),
                "Ошибка в работе сервера.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now());
    }
}