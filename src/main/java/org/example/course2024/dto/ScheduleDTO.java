package org.example.course2024.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ScheduleDTO(Long id,
                          LocalDateTime date,
                          String masterName, String status) {
}
