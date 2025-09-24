package com.oneonone.munsterlandbackend.models;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.hibernate.annotations.JdbcTypeCode;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity {

  @Id
  @GeneratedValue
  private UUID id;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(columnDefinition = "user_kind")
  private UserKind kind;   // VOLUNTEER / NEWCOMER / ADMIN

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(columnDefinition = "user_role")
  private UserRole role;   // STUDENT / VISITOR / PROFESSIONAL

  @Column(nullable = false, unique = true)   // DB has CITEXT unique; this is fine
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  private String city;

  @Column(name = "is_active", nullable = false)
  private boolean isActive = true;

  // Volunteer-ish
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(columnDefinition = "availability")
  private Availability availability;

  private Integer points;   // defaults to 0 in DB; null = let DB default apply

  // Newcomer-ish
  @Column(name = "arrival_date")
  private java.time.LocalDate arrivalDate;

  private String about;

  @Column(name = "created_at", updatable = false, insertable = false)
  private OffsetDateTime createdAt;

  @Column(name = "updated_at", insertable = false)
  private OffsetDateTime updatedAt;
}
