package com.oneonone.munsterlandbackend.services;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import com.oneonone.munsterlandbackend.respository.*;
import com.oneonone.munsterlandbackend.models.UserSummaryDto;
import com.oneonone.munsterlandbackend.models.UserKind;
import com.oneonone.munsterlandbackend.db.QuestionEntity;
import java.util.UUID;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserQueryService {
    private final UserRepository users;
    private final QuestionRepository questions;
    private final AnswerRepository answers;

    public Page<UserSummaryDto> listNewcomers(Pageable pageable) {
        return users.findAllByKind(UserKind.NEWCOMER, pageable)
                .map(u -> {
                    long totalQ = questions.countByNewcomerId(u.getId());

                    var qIds = questions.findAllByNewcomerId(u.getId())
                                        .stream()
                                        .map(QuestionEntity::getId)
                                        .collect(Collectors.toList());

                    long answeredQ = qIds.isEmpty() ? 0 : answers.countByQuestionIdIn(qIds);

                    return new UserSummaryDto(
                            u.getId(),
                            u.getFullName(),
                            u.getEmail(),
                            u.getCity(),
                            u.getRole(),
                            u.getArrivalDate(),
                            u.getAbout(),
                            totalQ,
                            answeredQ
                    );
                });
    }
}
