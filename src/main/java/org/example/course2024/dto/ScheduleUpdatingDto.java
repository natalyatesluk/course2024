package org.example.course2024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleUpdatingDto(Long id, @FutureOrPresent LocalDateTime date, StatusTime status,
                                  Long masterId) implements Serializable {
}