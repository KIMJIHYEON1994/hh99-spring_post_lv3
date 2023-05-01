package com.sparta.spring_post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor      // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성해줌
public class PostRequestDto {
    private String title;
    private String content;

}
