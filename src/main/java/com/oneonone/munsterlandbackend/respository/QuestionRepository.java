package com.oneonone.munsterlandbackend.respository;

import java.util.UUID;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.oneonone.munsterlandbackend.models.QuestionStatus;
import com.oneonone.munsterlandbackend.db.QuestionEntity;
import com.oneonone.munsterlandbackend.models.QuestionResponse;

public interface QuestionRepository extends JpaRepository<QuestionEntity,UUID>{
    long countByNewcomerId(UUID newCommerId);
      // If you also need the actual list of questions:
      List<QuestionEntity> findAllByNewcomerId(UUID newcomerId);
      Page<QuestionEntity> findAllByNewcomerId(UUID newcomerId, Pageable pageable);
      Page<QuestionEntity> findAllByNewcomerIdAndStatus(UUID newcomerId, QuestionStatus status, Pageable pageable);
}
