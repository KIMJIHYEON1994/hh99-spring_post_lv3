package com.sparta.spring_post.jwt;

import com.sparta.spring_post.entity.RoleType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
// @Slf4j ( Simple Logging Facade for Java ) : 로깅에 대한 추상 레이어를 제공하는 인터페이스의 모음 ( 로깅 Facade )
//                                             추상 로깅 프레임워크이기 때문에 단독으로 사용하지 않음
// 인터페이스를 사용하여 로깅을 구현하게 되면 추후에 필요로 의해 로깅 라이브러리를 변경할 때 코드의 변경 없이 가능함
// 로깅 : 시스템 동작 시 시스템 상태 / 작동 정보를 시간의 경과에 따라 기록하는 것 ( 그 기록을 로그 라고 함 )
@Component
// @Component : Bean Configuration 파일에 Bean 을 따로 등록하지 않아도 사용할 수 있음
//              -> 빈 등록을 빈 클래스 자체에다가 할 수 있음
@RequiredArgsConstructor
// @RequiredArgsConstructor : final 이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 lombok 어노테이션
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    // JWT 토큰 인증 정보를 전송하는데 사용되는 HTTP 헤더의 이름을 설정
    // String AUTHORIZATION_HEADER = BEARER_PREFIX + token
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;         // 1시간

    @Value("${jwt.secret.key}")
    // Spring Security 에서 JWT 를 인코딩 및 디코딩하는데 사용되는 비밀키
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    // HS256 대칭 키 ( 토큰 서명과 확인에 동일한 키가 사용됨 )와 SHA-256 해싱 알고리즘을 사용하여 서명을 생성

    @PostConstruct
    // @PostConstruct : 의존성 주입이 이루어진 후 초기화를 수행하는 메서드에 사용
    // 생성자가 호출 되었을 때 Bean 은 아직 초기화 되지 않음
    // -> @PostConstruct 사용하면 Bean 이 초기화 됨과 동시에 의존성을 확인할 수 있음
    // -> Bean 이 생성될 때 딱 한 번 초기화 하기 때문에 여러번 초기화되는 것을 방지할 수 있음
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        // Base64 클래스를 사용하여 인코딩된 문자열을 바이트 배열로 디코딩함
        key = Keys.hmacShaKeyFor(bytes);
        // JWT 를 인코딩 및 디코딩하는데 사용할 비밀 키를 만듦 ( 비밀 키 생성 )
    }

    // header 토큰을 가져오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            // StringUtils.hasText(bearerToken) : 문자열이 null 이 아니며 공백이 아닌 문자가 하나 이상 포함되어 있는지 확인
            // bearerToken.startsWith(BEARER_PREFIX) : 문자열이 변수 값 ( BEARER_PREFIX ) 으로 시작하는지 확인
            return bearerToken.substring(7);
            // 처음 7자를 제외한 문자열을 반환
        }
        return null;
    }

    // 토큰 생성
    public String createToken(String username, RoleType role) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()      // JWT 내용 및 서명을 지정하는데 사용할 수 있는 새 인스턴스를 만듦
                        .setSubject(username)           // JWT 의 제목을 username 문자열로 만듦
                        .claim(AUTHORIZATION_KEY, role)         // Auth 키와 role 변수 값이 있는 클레임이 포함됨
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))       // 만료시간을 설정
                        .setIssuedAt(date)              // 발행 시간을 설정
                        .signWith(key, signatureAlgorithm)      // 이전에 지정된 비밀 키와 알고리즘을 사용하여 JWT 서명하기 위해 호출
                                                                // 나중에 동일한 비밀 키와 알고리즘을 사용하여 JWT 를 확인할 수 있음
                        .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // parserBuilder() : 새 인스턴스를 만듦
            // setSigningKey(key) : JWT 의 서명을 확인하는데 사용되는 비밀 키 설정
            // build() : 지금까지의 내용을 바탕으로 인스턴스를 빌드하는 메서드
            // parseClaimsJws(token) : 지정된 비밀 키를 사용하여 JWT 의 서명을 확인하고 분석된 JWT 정보를 모두 포함하는 객체를 반환
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // MalformedJwtException : 토큰에 들어온 토큰값이 올바르지 않음
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            // ExpiredJwtException : JWT 생성할 때 지정한 유효기간을 초과
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            // UnsupportedJwtException : JWT 가 예상하는 형식과 다른 형식이거나 구성
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            // IllegalArgumentException : 적합하지 않거나 ( illegal ) 적절하지 못한 ( inappropriate ) 인자를 메서드에 넘겨줌
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        // parserBuilder() : 새 인스턴스를 만듦
        // setSigningKey(key) : JWT 의 서명을 확인하는데 사용되는 비밀 키 설정
        // build() : 지금까지의 내용을 바탕으로 인스턴스를 빌드하는 메서드
        // parseClaimsJws(token) : 지정된 비밀 키를 사용하여 JWT 의 서명을 확인하고 분석된 JWT 정보를 모두 포함하는 객체를 반환
        // getBody() : JWT 토큰의 payload 를 객체로 가져옴
    }

}
