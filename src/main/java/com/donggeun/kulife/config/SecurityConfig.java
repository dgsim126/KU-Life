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

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login","/join").permitAll()  // 로그인 없이 접근 가능
                        .requestMatchers("/admin").hasRole("ADMIN")    // 해당 role만 접근 가능
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated() // 위에서 처리되지 못한 경우 로그인 된 경우만 접근 가능
                );

        // 커스텀 로그인
        http
                .formLogin((auth) ->
                        auth.loginPage("/login")            // 로그인 경로
                        .loginProcessingUrl("/loginProc")   // 로그인 처리 요청 경로(Spring Security가 내부적으로 처리하므로, 별도의 컨트롤러를 작성할 필요가 없음)
                        .permitAll()                        // 위의 두 경로는 인증 없이 접근 가능
                );

        // CSRF(Cross Site Request Forgery: 사이트 간 요청 위조)
//        http
//                .csrf((auth) -> auth.disable()); // 개발환경에서 csrf 비활성화


        // CSRF 활성화 시 로그아웃을 GET 요청으로 하기 위해
        http
                .logout((auth) -> auth.logoutUrl("/logout")
                        .logoutSuccessUrl("/"));

        
        // 다중 로그인 설정
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true));

        
        // 세션 고정 공격 보호
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());
        
        


        return http.build();
    }
}
