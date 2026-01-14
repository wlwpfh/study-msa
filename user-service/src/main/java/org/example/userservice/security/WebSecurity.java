package org.example.userservice.security;

import org.example.userservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    private UserService userService;
    private Environment env;

    public WebSecurity(UserService userService, Environment env) {
        this.userService = userService;
        this.env = env;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(auth ->  auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults()) // for basic authentication
                .headers((header) -> header
                        .frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        return http.build();
    }
}
