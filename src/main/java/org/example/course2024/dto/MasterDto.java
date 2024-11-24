package org.example.course2024.dto;

import org.example.course2024.entity.Master;

import java.io.Serializable;

/**
 * DTO for {@link Master}
 */
public record MasterDto(Long id, String name, String surname, String middleName, String phone, String email,
                        int doneWork) implements Serializable {
}