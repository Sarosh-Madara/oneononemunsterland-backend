package com.oneonone.munsterlandbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.oneonone.munsterlandbackend.models.LinkResponse;
import com.oneonone.munsterlandbackend.services.LinkService;
import com.oneonone.munsterlandbackend.models.LinkRequest;
import lombok.RequiredArgsConstructor;
import com.oneonone.munsterlandbackend.models.LinkDetailsResponse;
import java.util.UUID;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;


    @PostMapping
    public ResponseEntity<LinkResponse> create(@RequestBody LinkRequest req){
       return ResponseEntity.ok(linkService.createLink(req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LinkDetailsResponse> unlink(@PathVariable UUID id) {
        return ResponseEntity.ok(linkService.unlink(id));
    }
}
