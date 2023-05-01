package com.sparta.spring_post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sparta.spring_post.dto.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity         // @Entity : DB 의 테이블과 일대일로 매칭되는 객체 단위
                // Entity 객체의 인스턴스 하나가 테이블에서 하나의 레코드 값을 의미함
@NoArgsConstructor      // @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성
public class Comment extends Timestamped {

    @Id         // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @GeneratedValue : 새로운 레코드가 생성될때마다 마지막 PK 값에서 자동으로 +1 해줌
    // strategy = GenerationType. : PK 값에 대한 생성 전략
    // AUTO : JPA 구현체가 자동으로 생성전략을 결정함
    // IDENTITY : 기본키 생성을 DB 에 위임함
    // SEQUENCE : DB 의 특별한 오브젝트 시퀀스를 사용하여 기본키를 생성함
    // TABLE : DB 에 키 생성 전용 테이블을 하나 만들고 이를 사용하여 기본키를 생성함
    private Long id;

    @ManyToOne      // 다대일 연관관계 ( JPA 어노테이션 )
    @JoinColumn(name = "post_id", nullable = false)
    // @JoinColumn : 관계에 사용될 열을 지정하는데 사용됨
    @JsonIgnore
    // @JsonIgnore : 데이터를 주고받을 때, 해당 데이터 ignore. 응답값 보이지 않음
    private Post post;

    @Column(nullable = false)
    // @Column : DB 의 테이블에 있는 컬럼과 동일하게 1대1로 매칭되기 때문에 Entity 클래스안에 내부변수로 정의됨
    // 별다른 옵션을 설정하지 않으면 생략 가능함
    // nullable = false : null 허용 X
    // nullable = true : null 허용 O
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_name", nullable = false)
    @JsonManagedReference
    // 순환참조를 방어하기 위한 어노테이션
    // 부모 클래스 -> @JsonManagedReference
    // 자식 클래스 -> @JsonBackReference
    // 순환참조 해결방안
    // 1. @JsonIgnore
    // 2. @JsonManagedReference & @JsonBackReference
    // 3. DTO 사용
    // 4. 양방향 매핑이 굳이 필요없다면 단방향 매핑으로 수정
    private Users users;

    public Comment(Users user, CommentRequestDto commentRequestDto, Post post) {
        this.post = post;
        this.users = user;
        this.content = commentRequestDto.getContent();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

}
