package com.sparta.spring_post.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Users {
    @Id
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    // @Enumerated : enum 유형이 DB 에 유지되는 방법을 지정하는데 사용되는 JPA 어노테이션
    // EnumType.STRING : EnumType 의 enum 값이 DB 에서 문자열로 유지되어야 함
    private RoleType role;

    public Users(String username, String password, RoleType role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
