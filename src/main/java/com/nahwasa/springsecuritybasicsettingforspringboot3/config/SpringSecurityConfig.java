package com.nahwasa.springsecuritybasicsettingforspringboot3.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
//        return new SimplePasswordEncoder();
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable()
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/status", "/images/**", "/view/join", "/auth/join").permitAll() // 인증 예외 처리할 케이스
//                        .requestMatchers("/view/setting/admin").hasRole("ADMIN")
//                        .requestMatchers("/view/setting/user").hasRole("USER")
                        .anyRequest().authenticated() // 어떠한 요청이라도 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/view/login") // 커스텀 로그인 페이지 지정
                        .loginProcessingUrl("/login-process") // submit 받을 url
                        .usernameParameter("userid") // submit 할 아이디
                        .passwordParameter("pw") // submit 할 비밀번호
                        .defaultSuccessUrl("/view/dashboard", true) // form 방식 로그인 사용
                        .permitAll() // 대시보드 이동은 허용해 줌
                )
                .logout(withDefaults()); // 로그아웃은 기본설정으로 (/logout 으로 인증 해제)

        return http.build();
    }

}
