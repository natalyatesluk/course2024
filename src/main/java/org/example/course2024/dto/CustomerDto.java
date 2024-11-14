package org.example.course2024.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.course2024.enums.PartBody;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Customer}
 */
public record CustomerDto(Long id, @NotNull @Size(max = 255) @NotEmpty @NotBlank String name,
                          @NotNull @Size(max = 255) @NotEmpty @NotBlank String surname,
                          @NotNull @Size(max = 255) @NotEmpty @NotBlank String middleName,
                          @NotNull @Size(min = 8, max = 20) @NotEmpty @NotBlank String phone,
                          @NotNull PartBody partBody) implements Serializable {
}