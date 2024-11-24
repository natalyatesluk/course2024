package org.example.course2024.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import org.example.course2024.entity.Appointment;
import org.example.course2024.enums.StatusAppoint;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for {@link Appointment}
 */
public record AppointmentUpdatingDto(@Positive Long id, Long customerId, Long scheduleId, @FutureOrPresent LocalDate localDate,   @FutureOrPresent LocalTime localTime,Long masterId,
                                     StatusAppoint status, Long priceId, BigDecimal pricePrice) implements Serializable {
}