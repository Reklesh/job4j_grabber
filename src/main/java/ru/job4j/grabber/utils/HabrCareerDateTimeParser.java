package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabrCareerDateTimeParser implements DateTimeParser {
    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime originalDate = LocalDateTime.parse(parse, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String targetDate = originalDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return LocalDateTime.parse(targetDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
