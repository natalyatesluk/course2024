package org.example.course2024.dto;

import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleDto(Long id, LocalDateTime date, StatusTime status, Long masterId, String masterSurname,
                          String masterPhone) implements Serializable {
}