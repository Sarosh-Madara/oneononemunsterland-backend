package com.oneonone.munsterlandbackend.models;
import java.util.UUID;

public record QuestionResponse(UUID id,
UUID newcomerId,
Integer typeId,
String title,
String body,
QuestionStatus status) {

}
