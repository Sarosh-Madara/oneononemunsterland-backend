package com.oneonone.munsterlandbackend.models;

public record LoginResponse(
        String token,
        String id,
        String email,
        String fullName,
        String kind,
        String role
) {}
