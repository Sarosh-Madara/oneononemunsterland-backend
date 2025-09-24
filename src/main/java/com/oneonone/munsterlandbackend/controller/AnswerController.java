package com.oneonone.munsterlandbackend.controller;
import com.oneonone.munsterlandbackend.models.AnswerRequest;
import com.oneonone.munsterlandbackend.models.AnswerResponse;
import com.oneonone.munsterlandbackend.services.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponse> post(@RequestBody AnswerRequest req) {
        return ResponseEntity.ok(answerService.postAnswer(req));
    }
}
