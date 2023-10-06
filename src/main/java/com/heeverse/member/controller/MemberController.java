package com.heeverse.member.controller;

import com.heeverse.common.IndexController;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static com.heeverse.common.IndexController.*;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> register(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        memberService.signup(memberRequestDto);
        return ResponseEntity.created(URI.create(INDEX_URI)).build();
    }
}
