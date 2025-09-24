package com.oneonone.munsterlandbackend.models;
import java.time.OffsetDateTime;
import java.util.UUID;

public record QuestionSummaryDto(
        UUID id,
        Integer typeId,
        String title,
        String body,
        QuestionStatus status,
        OffsetDateTime createdAt,
        boolean hasAnswer
) {}
