package com.oneonone.munsterlandbackend.models;

import java.time.LocalDate;
import java.util.UUID;

public record UserSummaryDto(
        UUID id,
        String fullName,
        String email,
        String city,
        UserRole role,
        LocalDate arrivalDate,
        String about,
        long totalQuestions,
        long answeredQuestions

) {}
