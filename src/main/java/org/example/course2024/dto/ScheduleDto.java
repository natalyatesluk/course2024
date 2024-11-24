package org.example.course2024.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleDto(Long id, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                          @JsonFormat(pattern = "HH:mm") LocalTime time, StatusTime status, Long masterId,
                          String masterSurname, String masterPhone) implements Serializable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public String formattedDate() {
        return date != null ? date.format(DATE_FORMATTER) : "";
    }

    public String formattedTime() {
        return time != null ? time.format(TIME_FORMATTER) : "";
    }
}
