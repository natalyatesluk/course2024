package org.example.course2024.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record PriceDTO(Long id, String masterName,
                       String size, BigDecimal price) implements Serializable {
}
