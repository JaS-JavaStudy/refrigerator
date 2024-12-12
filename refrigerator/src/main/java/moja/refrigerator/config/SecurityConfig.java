package moja.refrigerator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    // 비밀번호 암호화를 위한 인코더
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 보안 비활성화
        http
                .csrf((auth) -> auth.disable());

        // 기본 로그인 폼 비활성화
        http
                .formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 비활성화
        http
                .httpBasic((auth) -> auth.disable());

        // URL 별 접근 권한 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()  // 이 경로들은 모두 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")               // admin 경로는 ADMIN 역할을 가진 사용자만
                        .anyRequest().authenticated());                             // 나머지는 인증된 사용자만

        // 세션 관리 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // JWT 사용을 위한 세션리스 설정

        return http.build();
    }
}