package org.example.course2024.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.example.course2024.entity.Price}
 */
public record PriceCreationDto(@NotNull @Size(max = 255) @NotEmpty @NotBlank String size, @NotNull @Positive BigDecimal price, Long masterId) implements Serializable {
  }