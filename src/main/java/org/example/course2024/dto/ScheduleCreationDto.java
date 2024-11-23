package org.example.course2024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleCreationDto(@NotNull  @FutureOrPresent LocalDate date,@NotNull  @FutureOrPresent  LocalTime time,
                                  @NotNull StatusTime status, Long masterId) implements Serializable {
}