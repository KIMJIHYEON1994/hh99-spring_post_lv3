package com.sparta.spring_post.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sparta.spring_post.dto.PostRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    // @ManyToOne 관계로 매핑되어 있는 상황에서 fetch 타입을 줄 수 있음
    // (fetch = FetchType.EAGER) : 즉시 로딩
    // Post 를 조회하는 시점에 바로 Users 까지 불러오는 쿼리를 날려 한꺼번에 데이터를 불러옴
    // (fetch = FetchType.LAZY) : 지연 로딩
    // Post 를 조회하는 시점이 아닌 실제 Users 를 사용하는 시점에 쿼리를 날림
    // 가급적이면 기본적으로 지연 로딩을 사용하는 것이 좋음
    @JoinColumn(name = "user_name", nullable = false)
    private Users users;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    // mappedBy : One 쪽에 매핑되는 관계의 Many 쪽에 있는 필드를 지정함
    // cascade : 관계의 계단식 유형을 지정함
    // cascade = CascadeType.REMOVE : Post 엔티티가 제거되면 연결된 모든 Comment 엔티티도 제거됨
    @OrderBy("id asc")
    // "id" 필드를 기준으로 오름차순 정렬
    @JsonBackReference
    // 순환참조를 방어하기 위한 어노테이션
    // 부모 클래스 -> @JsonManagedReference
    // 자식 클래스 -> @JsonBackReference
    private List<Comment> comments;

    public Post(Users users, PostRequestDto postRequestDto) {
        this.users = users;
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

}
