package ru.practicum.ewm.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiError handleMethodValidationException(final HandlerMethodValidationException exception) {
        return new ApiError(exception.getMessage(),
                "Некорректный параметр в запросе",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiError handleNotUniqueValueFromDB(final DataIntegrityViolationException exception) {
        return new ApiError(exception.getMessage(),
                "Нарушение ограничения уникальности значения",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiError handleNotConstraintViolationExceptionFromDB(final ConstraintViolationException exception) {
        return new ApiError(exception.getMessage(),
                "Нарушение ограничения уникальности значения",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException exception) {
        return new ApiError(exception.getMessage(),
                "Нарушение установленных правил обращения к ресурсу",
                HttpStatus.CONFLICT,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleNotValidDateTimeException(final NotValidDateTimeException exception) {
        return new ApiError(exception.getMessage(),
                "Нарушение условий для запрашиваемой операции",
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ApiError handleNotFoundException(final NotFoundException exception) {
        return new ApiError(exception.getMessage(),
                "Указанный Id не найден",
                HttpStatus.NOT_FOUND,
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