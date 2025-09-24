package com.oneonone.munsterlandbackend.respository;

import com.oneonone.munsterlandbackend.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;
import com.oneonone.munsterlandbackend.models.UserKind;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
  boolean existsByEmail(String email);
  Optional<UserEntity> findByEmail(String email);
   // Derived query: Spring will generate SQL like
    // SELECT * FROM users WHERE kind = ? with pagination
  Page<UserEntity> findAllByKind(UserKind kind, Pageable pageable);
}
