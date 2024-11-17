package org.example.course2024.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Master}
 */
public record MasterCreationDto(@NotNull @Size(max = 255) @NotEmpty @NotBlank String name,
                                @NotNull @Size(max = 255) @NotEmpty @NotBlank String surname,
                                @NotNull @Size(max = 255) @NotEmpty @NotBlank String middleName,
                                @NotNull @Size(min = 8, max = 10) @NotEmpty @NotBlank String phone,
                                @Positive int doneWork) implements Serializable {
}