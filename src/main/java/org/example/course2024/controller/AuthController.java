package org.example.course2024.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.course2024.dto.JwtAuthenticationResponse;
import org.example.course2024.dto.SignInRequest;
import org.example.course2024.dto.SignUpRequest;
import org.example.course2024.dto.UserDto;
import org.example.course2024.entity.User;
import org.example.course2024.enums.Role;
import org.example.course2024.mapper.UserMapper;
import org.example.course2024.service.AuthenticationService;
import org.example.course2024.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(
            @PathVariable Long id,
            @RequestParam Role newRole) {
        User updatedUser = userService.updateUserRole(id, newRole);
        return ResponseEntity.ok(userMapper.toDto (updatedUser));
    }
}