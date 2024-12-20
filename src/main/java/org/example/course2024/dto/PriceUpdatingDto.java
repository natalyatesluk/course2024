package org.example.course2024.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.example.course2024.entity.Price;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Price}
 */
public record PriceUpdatingDto(@Positive Long id, @Size(max = 255) String size, @Positive BigDecimal price,
                               Long masterId) implements Serializable {
}