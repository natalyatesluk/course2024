package org.example.course2024.dto;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Master}
 */
public record MasterDto(Long id, String name, String surname, String middleName, String phone,
                        int doneWork) implements Serializable {
}