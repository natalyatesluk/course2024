package org.example.course2024.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentDTO(Long id, String customerSurname,
                             String customerPhone, String bodyPart, String size,
                             LocalDateTime appointmentDate,
                             String master, BigDecimal price
                             ) implements Serializable { }
