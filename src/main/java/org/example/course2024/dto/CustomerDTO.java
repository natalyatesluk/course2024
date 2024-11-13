package org.example.course2024.dto;

import java.io.Serializable;

public record CustomerDTO(Long id, String firstName, String lastName,
                          String phone, String master) implements Serializable {
}
