package org.example.course2024.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link org.example.course2024.entity.Price}
 */
public record PriceDto(Long id, String size, BigDecimal price, Long masterId, String masterName, String masterSurname) implements Serializable {
  }