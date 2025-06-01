package ru.practicum.ewm.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewm.util.EwmConstants.DATE_TIME_FORMAT;

public final class DateTimeMapper {

    public static LocalDateTime mapToDateTime(String date) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    public static String mapToString(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return DateTimeFormatter
                .ofPattern(DATE_TIME_FORMAT).format(date);
    }
}