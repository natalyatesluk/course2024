package org.example.course2024.dto;

import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleDto(Long id, LocalDate date, LocalTime time, StatusTime status, Long masterId, String masterSurname,
                          String masterPhone) implements Serializable {
}