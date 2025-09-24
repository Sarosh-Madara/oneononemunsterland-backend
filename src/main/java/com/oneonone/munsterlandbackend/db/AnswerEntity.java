package com.oneonone.munsterlandbackend.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "answers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AnswerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "question_id", nullable = false, unique = true)
    private UUID questionId;   // FK to questions.id

    @Column(name = "volunteer_id", nullable = false)
    private UUID volunteerId;  // FK to users.id (kind=VOLUNTEER)

    @Column(nullable = false)
    private String body;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private OffsetDateTime updatedAt;
}
