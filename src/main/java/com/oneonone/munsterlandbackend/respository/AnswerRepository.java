package com.oneonone.munsterlandbackend.respository;

import java.util.*;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oneonone.munsterlandbackend.db.AnswerEntity;

public interface AnswerRepository extends JpaRepository<AnswerEntity,UUID>{
    boolean existsByQuestionId(UUID questionId);
    long countByQuestionIdIn(Collection<UUID> questionIds);
    Optional<AnswerEntity> findByQuestionId(UUID questionId);
}
