package com.heeverse.member.controller;

import com.heeverse.member.dto.LoginDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gutenlee
 * @since 2023/07/23
 */
@RestController
public class LoginController {

    @PostMapping("/login")
    public String login(LoginDto loginDto) {
        return "done auth";
    }
}
