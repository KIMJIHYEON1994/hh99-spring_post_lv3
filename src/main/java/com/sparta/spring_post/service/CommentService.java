package com.sparta.spring_post.service;

import com.sparta.spring_post.dto.CommentRequestDto;
import com.sparta.spring_post.dto.UserResponseDto;
import com.sparta.spring_post.entity.Comment;
import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.entity.Users;
import com.sparta.spring_post.jwt.JwtUtil;
import com.sparta.spring_post.repository.CommentRepository;
import com.sparta.spring_post.repository.PostRepository;
import com.sparta.spring_post.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
// @Service : 해당 클래스를 루트 컨테이너에 빈 ( Bean) 객체로 생성해주는 어노테이션
// 부모 어노테이션인 @Component 를 붙여줘도 똑같이 루트 컨테이너에 생성되지만 가시성이 떨어지기 때문에 잘 사용하지 않음
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtUtil jwtUtil;

    // 댓글 등록
    @Transactional
    // Transaction : DB 관리 시스템 또는 유사한 시스템에서 상호작용의 단위 ( 더 이상 쪼개질 수 없는 최소의 연산 )
    // @Transactional : 해당 범위 내 메서드가 트랜잭션이 되도록 보장해줌 ( 선언적 트랜잭션 - 선언만으로 관리를 용이하게 해줌 )
    // (readOnly = true) : 읽기 전용 모드 - 예상치 못한 Entity 의 생성, 수정, 삭제를 예방할 수 있음
    public UserResponseDto<Comment> addComment(CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);

        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
        );

        Comment comment = new Comment(user, commentRequestDto, post);
        commentRepository.save(comment);
        return UserResponseDto.setSuccess("댓글이 등록되었습니다.");
    }

    // 댓글 수정
    @Transactional
    public UserResponseDto<Comment> updateComment(Long id, CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
                // IllegalArgumentException : 유효하지 않거나 잘못된 인수가 메서드에 전달될 때 발생하는 예외
                //                            일반적으로 메서드가 예상 값 범위에 속하지 않거나 특정 제약 조건을 위반하는 인수를 받을 때 발생함
        );

        if (comment.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            comment.update(commentRequestDto);
            return UserResponseDto.setSuccess("댓글이 수정되었습니다.");
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

    }

    // 댓글 삭제
    @Transactional
    public UserResponseDto<Comment> deleteComment(Long id, HttpServletRequest httpServletRequest) {
        Users user = checkJwtToken(httpServletRequest);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다.")
        );

        if (comment.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            commentRepository.delete(comment);
            return UserResponseDto.setSuccess("댓글 삭제 성공");
        } else {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

    }

    // 토큰 체크
    public Users checkJwtToken(HttpServletRequest request) {
        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        // jwtUtil.resolveToken(httpServletRequest) : httpServletRequest 객체에서 토큰을 추출하는 데 사용되는 메서드
        // 추출된 토큰은 디코딩하여 포함된 사용자 정보를 얻을 수 있음

        Claims claims;

        // 토큰이 있는 경우에만 게시글 접근 가능
        if (token != null) {
            if (jwtUtil.validateToken(token)) {
                // validateToken : JWT 토큰의 유효성을 검사하고, 올바른 서명을 가지고 있는지 발급자와 수신자가 유효한지 등을 확인함
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            Users user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );
            return user;

        }
        return null;
    }

}
