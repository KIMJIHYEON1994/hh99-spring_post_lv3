package com.sparta.spring_post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "set")
// @AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자를 만듦 ( Lombok 어노테이션 )
// staticName = "set" : 'set' 이라는 이름의 정적 팩토리 메소드 생성 ( 불변 객체 생성 )
public class UserResponseDto<D> {
    // 제네릭 : 클래스 내부에서 정하는 것이 아닌 사용자 호출에 의해 타입이 지정되는 것
    //         -> 특정 타입의 변수형에 지정되는 것이 아닌 필요에 의해 여러 가지 타입을 사용하고 싶을 경우 사용
    //         클래스 / 인터페이스 / 메서드 등의 타입을 파라미터로 사용할 수 있게 해주는 역할을 함
    //         비제네릭 타입의 코드에서 발생하는 불필요한 타입 변환으로 인한 프로그램 성능의 저하를 감소시킬 수 있음
    //         D 로 선언했지만 A,B,C ... 어떤 걸로 하든 상관 없음 ( 컨벤션은 있음 )
    private String message;
    private int statusCode;

    public static <D> UserResponseDto<D> setSuccess(String message) {
        return UserResponseDto.set(message, 200);
    }

    public static <D> UserResponseDto<D> setFailed(String message) {
        return UserResponseDto.set(message, 400);
    }

}
