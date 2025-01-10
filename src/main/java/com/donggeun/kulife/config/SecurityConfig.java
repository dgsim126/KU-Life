package com.donggeun.kulife.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 비밀번호 암호화
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 관련 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // 접근 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/", "/api/v1/login", "/api/v1/join").permitAll() // 로그인 없이 접근 가능
                        .requestMatchers("/api/v1/admin").hasRole("ADMIN") // 해당 role만 접근 가능
                        .requestMatchers("/api/v1/my/**").hasAnyRole("ADMIN", "USER") // /api/v1/my/**만 허용
                        .anyRequest().authenticated() // 위에서 처리되지 못한 경우 로그인 된 경우만 접근 가능
                );

        // 커스텀 로그인
        http
                .formLogin((auth) -> auth
                        // .loginPage("/api/v1/login") // 로그인 경로
                        .loginProcessingUrl("/api/v1/loginProc") // 로그인 처리 요청 경로
                        // .defaultSuccessUrl("/api/v1/", true) // 로그인 성공 후 리디렉션 경로 설정
                        .permitAll()
                );
        
        // 로그인 후, 리디렉션이 아닌 JSON 응답 반환을 위해
        http
                .formLogin((auth) -> auth
                        .loginProcessingUrl("/api/v1/loginProc")
                        .successHandler((request, response, authentication) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\": \"Login successful\"}");
                            response.getWriter().flush();
                        })
                        .permitAll()
                )
                .csrf((auth) -> auth.disable());


        // CSRF(Cross Site Request Forgery: 사이트 간 요청 위조)
        http
                .csrf((auth) -> auth.disable()); // 개발환경에서 csrf 비활성화


        // CSRF 활성화 시 로그아웃을 GET 요청으로 하기 위해
        http
                .logout((auth) -> auth
                        .logoutUrl("/api/v1/logout") // 로그아웃 경로
                        .logoutSuccessUrl("/api/v1/") // 로그아웃 성공 시 리다이렉트 경로
                );

        // 다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                );

        // 세션 고정 공격 보호
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()
                );

        return http.build();
    }
}
