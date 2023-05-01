package com.sparta.spring_post.controller;

import com.sparta.spring_post.dto.LoginRequestDto;
import com.sparta.spring_post.dto.SignupRequestDto;
import com.sparta.spring_post.dto.UserResponseDto;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController                         // @RestController : 단순히 객체만들 반환하고 객체 데이터는 JSON 또는 XML 형식으로 HTTP 응답에 담아서 전송함
@RequiredArgsConstructor                // @RequiredArgsConstructor : final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 lombok 어노테이션
@RequestMapping("/api/user")         // @RequestMapping : 공통되는 URL 이 있을 경우 메서드에 중복되는 value 값을 없앨 수 있음
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public UserResponseDto<Users> signup(@RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인
    @PostMapping("/login")
    public UserResponseDto<Users> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(loginRequestDto, httpServletResponse);
    }

}
