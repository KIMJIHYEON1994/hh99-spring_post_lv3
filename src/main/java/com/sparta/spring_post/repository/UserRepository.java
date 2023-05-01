package com.sparta.spring_post.repository;

import com.sparta.spring_post.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    // Optional : 객체가 null 값을 포함할 수 있는지 여부를 나타내는 클래스 ( NPE (NullPointerException) 을 방지 )
    //            null 이나 누락된 값을 보다 안전하게 처리할 수 있는 장점이 있음

}
