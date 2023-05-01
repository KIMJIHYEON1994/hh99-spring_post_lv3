package com.sparta.spring_post.controller;

import com.sparta.spring_post.dto.PostRequestDto;
import com.sparta.spring_post.dto.PostResponseDto;
import com.sparta.spring_post.dto.UserResponseDto;
import com.sparta.spring_post.entity.Post;
import com.sparta.spring_post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController                     // @RestController : 단순히 객체만들 반환하고 객체 데이터는 JSON 또는 XML 형식으로 HTTP 응답에 담아서 전송함
@RequiredArgsConstructor            // @RequiredArgsConstructor : final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 lombok 어노테이션
@RequestMapping("/api")          // @RequestMapping : 공통되는 URL 이 있을 경우 메서드에 중복되는 value 값을 없앨 수 있음
public class PostController {

    private final PostService postService;

    // 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // 상세 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        // @PathVariable : URL 경로에 변수를 넣어주는 어노테이션
        return postService.getPost(id);
    }

    // 추가
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        // @RequestBody : 클라이언트가 전송하는 JSON 형태의 HTTP Body 내용을 Java 객체로 변환시켜줌
        return postService.createPost(postRequestDto, httpServletRequest);
    }

    // 수정
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest httpServletRequest) {
        return postService.updatePost(id, postRequestDto, httpServletRequest);
    }

    // 삭제
    @DeleteMapping("/post/{id}")
    public UserResponseDto<Post> deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        return postService.deletePost(id, httpServletRequest);
    }

}
