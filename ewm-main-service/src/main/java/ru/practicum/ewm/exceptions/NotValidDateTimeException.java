package ru.practicum.ewm.exceptions;

public class NotValidDateTimeException extends RuntimeException {
    public NotValidDateTimeException(String message) {
        super(message);
    }
}
