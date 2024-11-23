package org.example.course2024.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link org.example.course2024.entity.Master}
 */
public record MasterUpdatingDto( Long id,
                                  String name,
                                  String surname,
                                  String middleName,
                                  String phone,
                                  String email,
                                  Integer doneWork ) implements Serializable {
}