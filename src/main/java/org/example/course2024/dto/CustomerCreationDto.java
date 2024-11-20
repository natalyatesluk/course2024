package org.example.course2024.dto;

import jakarta.validation.constraints.*;
import org.example.course2024.entity.Customer;
import org.example.course2024.enums.PartBody;

import java.io.Serializable;

/**
 * DTO for {@link Customer}
 */
public record CustomerCreationDto(
        @NotNull @Size(max = 255) @NotEmpty @NotBlank String name,
        @NotNull @Size(max = 255) @NotEmpty @NotBlank String surname,
        @NotNull @Size(max = 255) @NotEmpty @NotBlank String middleName,
        @NotNull @Size(min = 8, max = 10) @NotEmpty @NotBlank String phone,
        @NotNull @Email @Size(max = 255) @NotEmpty @NotBlank String email,
        @NotNull PartBody partBody
) implements Serializable {
}