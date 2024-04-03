package com.driver.Config;

import com.driver.Config.JWTSecurity.JwtAuthenticationEntryPoint;
import com.driver.Config.JWTSecurity.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;


    // Configuration for Security Filters
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Disable CSRF, set up URL-based authorization, handle exceptions, and configure session management
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration corsConfiguration = new CorsConfiguration();
                            corsConfiguration.setAllowCredentials(true);
                            corsConfiguration.addAllowedOriginPattern("*"); // * for testing and local
                            corsConfiguration.addAllowedHeader("Authorization");
                            corsConfiguration.addAllowedHeader("Content-Type");
                            corsConfiguration.addAllowedHeader("Accept");
                            corsConfiguration.addAllowedMethod("POST");
                            corsConfiguration.addAllowedMethod("GET");
                            corsConfiguration.addAllowedMethod("DELETE");
                            corsConfiguration.addAllowedMethod("PUT");
                            corsConfiguration.addAllowedMethod("OPTIONS");
                            corsConfiguration.setMaxAge(3600L);
                            return corsConfiguration;
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/home/**").authenticated()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add JWT Authentication Filter before UsernamePasswordAuthenticationFilter
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
