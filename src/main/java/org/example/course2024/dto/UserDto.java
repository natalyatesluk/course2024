package org.example.course2024.dto;

import org.example.course2024.entity.User;
import org.example.course2024.enums.Role;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
public record UserDto(Long id, String username, Role role) implements Serializable {
  }