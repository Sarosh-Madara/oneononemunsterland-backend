package com.oneonone.munsterlandbackend.controller;

import com.oneonone.munsterlandbackend.models.*;
import com.oneonone.munsterlandbackend.services.QuestionService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponse> post(@RequestBody QuestionRequest req) {
        String uid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID newcomerId = UUID.fromString(uid);
        return ResponseEntity.ok(questionService.postQuestion(req,newcomerId));
    }


    @GetMapping("/mine")
    public Page<QuestionSummaryDto> mine(
            @RequestParam(required = false) QuestionStatus status,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        return questionService.myQuestions(status, pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponse> updatePost(@RequestBody QuestionRequest req, @PathVariable("id") String questionId){
        String uid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID newcomerId = UUID.fromString(uid);
        return ResponseEntity.ok(questionService.updateQuestion(req,newcomerId, UUID.fromString(questionId)));
    }

    // @PostMapping("/{newcomerId}")
    // public Page<QuestionResponse> getQuestions(@PathVariable String newcomerId){
    //     questionService.getQuestions(newcomerId).stream;
    // }
}
