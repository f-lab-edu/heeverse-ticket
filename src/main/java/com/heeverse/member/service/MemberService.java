package com.heeverse.member.service;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.domain.repository.MemberRepository;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.exception.DuplicatedMemberException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public MemberService(PasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }

    public void signup(MemberRequestDto memberRequestDto) {
        boolean existMember = isExistMember(memberRequestDto);
        if (existMember) {
            throw new DuplicatedMemberException();
        }

        Member member = Member.builder()
            .id(memberRequestDto.getId())
            .userName(memberRequestDto.getUserName())
            .password(passwordEncoder.encode(memberRequestDto.getPassword()))
            .email(memberRequestDto.getEmail())
            .build();
    }

    private boolean isExistMember(MemberRequestDto memberRequestDto) {
        return false;
    }
}
