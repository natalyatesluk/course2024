package org.example.course2024.dto;

import org.example.course2024.entity.Customer;
import org.example.course2024.enums.PartBody;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
public record CustomerDto(Long id, String name,
                           String surname,
                           String middleName,
       String phone,
                         String email,
                          PartBody partBody) implements Serializable {
}