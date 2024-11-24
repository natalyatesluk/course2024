package org.example.course2024.dto;

import org.example.course2024.entity.Price;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Price}
 */
public record PriceDto(Long id, String size, BigDecimal price, Long masterId, String masterName, String masterSurname) implements Serializable {
  }