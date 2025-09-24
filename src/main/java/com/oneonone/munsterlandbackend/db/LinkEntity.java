package com.oneonone.munsterlandbackend.db;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import com.oneonone.munsterlandbackend.enums.LinkStatus;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "links",
    uniqueConstraints = @UniqueConstraint(columnNames = {"volunteer_id", "newcomer_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LinkEntity {

    @Id
    @GeneratedValue
    @UuidGenerator           // <-- THIS tells Hibernate to assign a UUID BEFORE persist()
    private UUID id;

    @Column(name = "volunteer_id", nullable = false)
    private UUID volunteerId;

    @Column(name = "newcomer_id", nullable = false)
    private UUID newcomerId;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    // store as plain text; ensure the column exists in DB with a default if you want
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "link_status")
    private LinkStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;
}