package org.example.course2024.dto;

import jakarta.validation.constraints.*;
import org.example.course2024.entity.Customer;
import org.example.course2024.enums.PartBody;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
public record CustomerUpdatingDto(@Positive Long id, @Size(max = 255) String name, @Size(max = 255) String surname,
                                  @Size(max = 255) String middleName, @Size(min = 8, max = 15) String phone,
                                  @Email @Size(max = 255)  String email,
                                  PartBody partBody) implements Serializable {
}