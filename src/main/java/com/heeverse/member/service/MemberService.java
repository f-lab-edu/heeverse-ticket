package com.heeverse.member.service;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.domain.mapper.MemberMapper;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.exception.DuplicatedMemberException;
import java.util.Objects;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    public MemberService(PasswordEncoder passwordEncoder, MemberMapper memberMapper) {
        this.passwordEncoder = passwordEncoder;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public void signup(MemberRequestDto memberRequestDto) {
        if (isExistMember(memberRequestDto)) {
            throw new DuplicatedMemberException();
        }

        memberMapper.insertMember(Member.builder()
            .id(memberRequestDto.getId())
            .userName(memberRequestDto.getUserName())
            .password(passwordEncoder.encode(memberRequestDto.getPassword()))
            .email(memberRequestDto.getEmail())
            .build());

    }


    public Optional<Member> findMember(String id) {
        // TODO : memberMapper.findById(id) 로 변경
        return Optional.of(Member.builder().build());
    }


    private boolean isExistMember(MemberRequestDto memberRequestDto) {
        return !Objects.isNull(memberMapper.findById(memberRequestDto.getId()));
    }

}
