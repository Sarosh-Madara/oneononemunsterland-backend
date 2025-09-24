package com.oneonone.munsterlandbackend.models;
import java.util.UUID;

public record AnswerResponse(UUID id, UUID questionId, UUID volunteerId, String body) {

}
