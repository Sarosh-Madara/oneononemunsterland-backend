package com.oneonone.munsterlandbackend.services;
import com.oneonone.munsterlandbackend.models.*;
import com.oneonone.munsterlandbackend.respository.QuestionRepository;
import com.oneonone.munsterlandbackend.respository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.oneonone.munsterlandbackend.db.QuestionEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import com.oneonone.munsterlandbackend.respository.AnswerRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questions;
    private final UserRepository users;
    private final AnswerRepository answers;

    public Page<QuestionSummaryDto> myQuestions(QuestionStatus status, Pageable pageable) {
        String uidStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID newcomerId = UUID.fromString(uidStr);
        Page<QuestionEntity> page = questions.findAllByNewcomerId(newcomerId, pageable);
        // Page<QuestionEntity> page = (status == null)
        //         ? questions.findAllByNewcomerId(newcomerId, pageable)
        //         : questions.findAllByNewcomerIdAndStatus(newcomerId, status, pageable);

        return page.map(q -> new QuestionSummaryDto(
                q.getId(),
                q.getTypeId(),
                q.getTitle(),
                q.getBody(),
                q.getStatus(),
                q.getCreatedAt(),
                answers.existsByQuestionId(q.getId())
        ));
    }
    
    @Transactional
    public QuestionResponse updateQuestion(QuestionRequest req, UUID newcomerId, UUID questionId){
        // Optional<QuestionEntity> questionOp = questions.findById(questionId);

        
        var entity = QuestionEntity.builder()
                .id(questionId)
                .newcomerId(newcomerId)
                .typeId(req.typeId())
                .title(req.title())
                .body(req.body())
                .status(QuestionStatus.OPEN)
                .build();
    
        var saved = questions.save(entity);
    
        return new QuestionResponse(
                saved.getId(),
                saved.getNewcomerId(),
                saved.getTypeId(),
                saved.getTitle(),
                saved.getBody(),
                saved.getStatus()
        );

    }

    @Transactional
    public QuestionResponse postQuestion(QuestionRequest req, UUID newcomerId) {
        var newcomer = users.findById(newcomerId)
            .orElseThrow(() -> new IllegalArgumentException("Newcomer not found"));
    
        if (newcomer.getKind() != UserKind.NEWCOMER) {
            throw new IllegalArgumentException("Only NEWCOMER can post questions");
        }
    
        var entity = QuestionEntity.builder()
                .newcomerId(newcomerId)
                .typeId(req.typeId())
                .title(req.title())
                .body(req.body())
                .status(QuestionStatus.OPEN)
                .build();
    
        var saved = questions.save(entity);
    
        return new QuestionResponse(
                saved.getId(),
                saved.getNewcomerId(),
                saved.getTypeId(),
                saved.getTitle(),
                saved.getBody(),
                saved.getStatus()
        );
    }
}
