package com.heeverse.member.controller;

import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    public ResponseEntity<HttpStatus> register(MemberRequestDto memberRequestDto) {
        return null;
    }
}
