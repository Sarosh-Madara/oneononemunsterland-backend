package com.oneonone.munsterlandbackend.db;

import com.oneonone.munsterlandbackend.models.QuestionStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "questions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class QuestionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "newcomer_id", nullable = false)
    private UUID newcomerId;   // FK to users.id (kind=NEWCOMER)

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "type_id", nullable = false)
    private Integer typeId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "question_status")
    private QuestionStatus status;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    private OffsetDateTime updatedAt;
}
