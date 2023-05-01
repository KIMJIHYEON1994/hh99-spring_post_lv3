package com.sparta.spring_post.controller;

import com.sparta.spring_post.dto.CommentRequestDto;
import com.sparta.spring_post.dto.UserResponseDto;
import com.sparta.spring_post.entity.Comment;
import com.sparta.spring_post.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController                     // @RestController : 단순히 객체만들 반환하고 객체 데이터는 JSON 또는 XML 형식으로 HTTP 응답에 담아서 전송함
@RequestMapping("/api")          // @RequestMapping : 공통되는 URL 이 있을 경우 메서드에 중복되는 value 값을 없앨 수 있음
@RequiredArgsConstructor            // @RequiredArgsConstructor : final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 lombok 어노테이션
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment")
    public UserResponseDto<Comment> addComment(@RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        return commentService.addComment(commentRequestDto, httpServletRequest);
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    public UserResponseDto<Comment> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest httpServletRequest) {
        // @PathVariable : URL 경로에 변수를 넣어주는 어노테이션
        // @RequestBody : 클라이언트가 전송하는 JSON 형태의 HTTP Body 내용을 Java 객체로 변환시켜줌
        return commentService.updateComment(id, commentRequestDto, httpServletRequest);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public UserResponseDto<Comment> deleteComment(@PathVariable Long id, HttpServletRequest httpServletRequest){
        return commentService.deleteComment(id, httpServletRequest);
    }

}
