package ru.practicum.server.exceptions;

public class NotValidDateTimeException extends RuntimeException {
    public NotValidDateTimeException(String message) {
        super(message);
    }
}
