package com.oneonone.munsterlandbackend.models;

import java.time.OffsetDateTime;
import com.oneonone.munsterlandbackend.enums.LinkStatus;
import java.util.UUID;

public record LinkDetailsResponse(
        UUID id,
        UUID volunteerId,
        UUID newcomerId,
        LinkStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime endedAt
) {}