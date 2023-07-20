package com.heeverse.member.controller;

import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        return new ResponseEntity<>(memberService.signup(memberRequestDto));
    }
}
