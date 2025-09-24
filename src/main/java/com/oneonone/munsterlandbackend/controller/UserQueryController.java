package com.oneonone.munsterlandbackend.controller;

import com.oneonone.munsterlandbackend.models.UserSummaryDto;
import com.oneonone.munsterlandbackend.services.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/newcomers")
    public Page<UserSummaryDto> getNewcomers(@PageableDefault(size = 20) Pageable pageable) {
        return userQueryService.listNewcomers(pageable);
    }
}

