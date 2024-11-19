package org.example.course2024.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Master}
 */
public record MasterUpdatingDto(@Positive Long id, @Size(max = 255) String name, @Size(max = 255) String surname,
                                @Size(max = 255) String middleName, @Size(min = 8, max = 15) String phone,
                                @Positive int doneWork) implements Serializable {
}