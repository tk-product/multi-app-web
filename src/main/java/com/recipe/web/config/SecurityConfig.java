package com.recipe.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("★★★securityFilterChain : ");
        http
                // CSRF対策は用途に応じて
                .csrf(csrf -> csrf.disable())

                // デフォルトのログインフォームを無効化
                .formLogin(form -> form.loginPage("/local-login")   // 独自のログインページ
                        .permitAll())

                // 必要に応じてHTTP Basic認証を無効化
                .httpBasic(httpBasic -> httpBasic.disable())

                // 認可設定（全て許可など）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}
