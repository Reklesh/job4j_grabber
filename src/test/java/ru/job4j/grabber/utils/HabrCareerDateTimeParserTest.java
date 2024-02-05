package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {

    @Test
    void checkParse() {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        String originalDate = "2024-01-25T18:10:31+03:00";
        LocalDateTime dateTime = dateTimeParser.parse(originalDate);
        assertThat(dateTime.toString()).isEqualTo("2024-01-25T18:10:31");
    }
}