package com.sparta.spring_post.dto;

import lombok.Getter;

@Getter         // Lombok 어노테이션. private 변수에 접근하기 위해 사용함
// @Getter : 인스턴스 변수를 반환 - 변수 앞에 get
// @Setter : 인스턴스 변수를 대입하거나 수정 - 변수 앞에 set
public class CommentRequestDto {
    private Long postId;
    private String content;

}
