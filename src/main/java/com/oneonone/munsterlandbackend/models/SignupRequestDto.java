package com.oneonone.munsterlandbackend.models;

import jakarta.validation.constraints.*;

public record SignupRequestDto(
    @NotBlank @Email String email,
    @NotBlank @Size(min = 6, max = 100) String password,
    @NotBlank String fullName,
    @NotNull UserKind kind,      // VOLUNTEER or NEWCOMER (ADMIN not allowed here)
    @NotNull UserRole role,      // STUDENT/VISITOR/PROFESSIONAL
    String city,
    String about
) {}
