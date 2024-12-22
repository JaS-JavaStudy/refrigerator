package moja.refrigerator.config;

import jakarta.servlet.http.HttpServletResponse;
import moja.refrigerator.jwt.JWTFilter;
import moja.refrigerator.jwt.JWTUtil;
import moja.refrigerator.jwt.LoginFilter;
import moja.refrigerator.jwt.LogoutFilter;
import moja.refrigerator.repository.user.TokenBlacklistRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    // JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final LogoutFilter logoutFilter;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, LogoutFilter logoutFilter, TokenBlacklistRepository tokenBlacklistRepository) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.logoutFilter = logoutFilter;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Bean
    // 비밀번호 암호화를 위한 인코더
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors((cors) -> cors.configure(http));

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
                        .requestMatchers("/**").permitAll()  // 이 경로들은 모두 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")  // admin 경로는 ADMIN 역할을 가진 사용자만
                        .anyRequest().authenticated());  // 나머지는 인증된 사용자만

        // 세션 관리 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // JWT 사용을 위한 세션리스 설정

        // 로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterAfter(new JWTFilter(jwtUtil, tokenBlacklistRepository), LoginFilter.class);

        http
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .addLogoutHandler(logoutFilter)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_OK);
                            response.getWriter().write("로그아웃 되었습니다.");
                        }));

        return http.build();
    }
}