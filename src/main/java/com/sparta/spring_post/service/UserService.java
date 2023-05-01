package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.LoginRequestDto;
import com.sparta.spring_post.dto.SignupRequestDto;
import com.sparta.spring_post.dto.UserResponseDto;
import com.sparta.spring_post.entity.RoleType;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.jwt.JwtUtil;
import com.sparta.spring_post.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    @Transactional
    public UserResponseDto<Users> signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        // 아이디 형식 확인
        if (!Pattern.matches("^(?=.*[a-z])(?=.*\\d)[a-z0-9]{4,10}$", username)) {
            // 정규표현식을 사용하여 username 문자열 유효성 검사
            // 소문자 ( a-z ), 숫자 ( 0-9 ) 로만 구성되어야 하며 길이는 4~10자 사이여야 함
            // ^ : 시작, $ : 끝
            // ㄱ-ㅎ가-힣 : 한글 문자
            // \\d : 0-9 숫자, \\D : \\d 가 아닌 것
            // \\w : [A-Za-z0-9_], \\W : \\w 가 아닌 것
            return UserResponseDto.setFailed("형식에 맞지 않는 아이디 입니다.");
        }

        // 비밀번호 형식 확인
        if (!Pattern.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[~!@#$%^&])[a-zA-Z\\d~!@#$%^&]{8,15}$", password)) {
            // 정규표현식을 사용하여 password 문자열 유효성 검사
            // 대문자 ( A-Z ), 소문자 ( a-z ), 숫자 ( 0-9 ), 특수문자로만 구성되어야 하며 길이는 8~15자 사이여야 함
            return UserResponseDto.setFailed("형식에 맞지 않는 비밀번호 입니다.");
        }

        // 회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            // isPresent() : Optional 객체가 값을 가지고 있다면 true, 값이 없다면 false 리턴 ( boolean 타입 )
            return UserResponseDto.setFailed("중복된 사용자입니다.");
        }

        // 관리자 확인
        RoleType role = RoleType.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                return UserResponseDto.setFailed("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = RoleType.ADMIN;
        }

        Users users = new Users(username, password, role);
        userRepository.save(users);
        return UserResponseDto.setSuccess("회원가입 성공!");
    }

    @Transactional(readOnly = true)
    public UserResponseDto<Users> login(LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 아이디 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (!found.isPresent()) {
            return UserResponseDto.setFailed("회원을 찾을 수 없습니다.");
        }

        Users user = userRepository.findByUsername(username).orElseThrow();
        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            return UserResponseDto.setFailed("일치하지 않는 비밀번호 입니다.");
        }

        httpServletResponse.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.getUsername(), user.getRole()));
        // JWT 토큰을 생성하고 HTTP 응답 헤더에 추가
        return UserResponseDto.setSuccess("로그인 성공!");
    }

}
