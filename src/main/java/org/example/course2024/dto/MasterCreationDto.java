package org.example.course2024.dto;

import jakarta.validation.constraints.*;
import org.example.course2024.entity.Master;

import java.io.Serializable;

/**
 * DTO for {@link Master}
 */
public record MasterCreationDto(@NotNull @Size(max = 255) @NotEmpty @NotBlank String name,
                                @NotNull @Size(max = 255) @NotEmpty @NotBlank String surname,
                                @NotNull @Size(max = 255) @NotEmpty @NotBlank String middleName,
                                @NotNull @Size(min = 8, max = 15) @NotEmpty @NotBlank String phone,
                                @NotNull @Email @Size(max = 255) @NotEmpty @NotBlank String email,
                                @Positive int doneWork) implements Serializable {
}