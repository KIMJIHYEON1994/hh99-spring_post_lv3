package com.sparta.spring_post.repository;

import com.sparta.spring_post.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // extends JpaRepository : Spring Data JPA 가 인터페이스에 대해서 프록시 구현체를 만든위 DI 받기 때문에 구현체가 없어도 동작할 수 있음
    // 미리 검색 메서드를 정의 해두는 것으로, 메서드를 호출하는 것만으로 효율적인 데이터 검색을 할 수 있게 되는 것
    // ( 일반적인 CRUD 메서드 ( save, find, delete 등 ) 제공 )
    // 메서드 이름을 지어주면 자동으로 JPA 가 메서드 이름을 분석해 적절한 JPQL 을 실행함
    // JPQL ( Java Persistence Query Language ) : Entity 객체를 조회하는 객체지향 쿼리
    // 테이블을 대상으로 쿼리하는 것이 아니라 엔티티 객체를 대상으로 쿼리함
}
