package org.example.course2024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.example.course2024.enums.StatusTime;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link org.example.course2024.entity.Schedule}
 */
public record ScheduleCreationDto(@NotNull  @FutureOrPresent LocalDateTime date,
                                  @NotNull StatusTime status, Long masterId) implements Serializable {
}