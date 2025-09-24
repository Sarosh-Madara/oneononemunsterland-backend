package com.oneonone.munsterlandbackend.models;

import java.util.UUID;

public record AnswerRequest(UUID questionId, String body) {

}
