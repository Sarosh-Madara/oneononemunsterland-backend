package com.oneonone.munsterlandbackend.services;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.oneonone.munsterlandbackend.respository.*;
import com.oneonone.munsterlandbackend.models.*;
import java.util.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.oneonone.munsterlandbackend.enums.*;
import com.oneonone.munsterlandbackend.db.*;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answers;
    private final QuestionRepository questions;
    private final UserRepository users;
    private final LinkRepository links;

    @Transactional
    public AnswerResponse postAnswer(AnswerRequest req) {
        // 1) Who is answering
        String uid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID volunteerId = UUID.fromString(uid);

        var volunteer = users.findById(volunteerId)
                .orElseThrow(() -> new IllegalArgumentException("Volunteer not found"));
        if (volunteer.getKind() != UserKind.VOLUNTEER) {
            throw new IllegalArgumentException("Only volunteers can answer");
        }

        // 2) Load question
        var question = questions.findById(req.questionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        // 3) Must be linked (ACTIVE) to this newcomer
        boolean linked = links.existsByVolunteerIdAndNewcomerIdAndStatus(
                volunteerId, question.getNewcomerId(), LinkStatus.ACTIVE);
        if (!linked) {
            throw new IllegalArgumentException("You must be linked to this newcomer to answer");
        }

        // 4) Only one answer per question
        if (answers.existsByQuestionId(question.getId())) {
            throw new IllegalArgumentException("This question already has an answer");
        }

        // 5) Save answer
        var saved = answers.save(AnswerEntity.builder()
                .questionId(question.getId())
                .volunteerId(volunteerId)
                .body(req.body())
                .build());

        // 6) Award +5 points to volunteer
        volunteer.setPoints(volunteer.getPoints() + 5);
        users.save(volunteer);

        // 7) (Optional) update question status → RESOLVED
        if (question.getStatus() == QuestionStatus.OPEN || question.getStatus() == QuestionStatus.IN_PROGRESS) {
            question.setStatus(QuestionStatus.RESOLVED);
            questions.save(question);
        }

        return new AnswerResponse(saved.getId(), saved.getQuestionId(), saved.getVolunteerId(), saved.getBody());
    }

}
