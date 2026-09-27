package com.jobportal.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> cors.configurationSource(
                    corsConfigurationSource()))

            .formLogin(form -> form.disable())

            .httpBasic(basic -> basic.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // Authentication
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // Recruiter-only job dashboard endpoint
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/jobs/recruiter"
                ).hasRole("RECRUITER")

                // Candidate profile
                .requestMatchers(
                    "/api/candidates/**"
                ).hasRole("CANDIDATE")

                // Candidate applications
                .requestMatchers(
                    "/api/applications/my"
                ).hasRole("CANDIDATE")

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/applications"
                ).hasRole("CANDIDATE")

                // Recruiter application management
                .requestMatchers(
                    "/api/applications/job/**",
                    "/api/applications/*/status",
                    "/api/applications/*/resume",
                    "/api/applications/*/resume/download"
                ).hasRole("RECRUITER")

                // Recruiter profile
                .requestMatchers(
                    "/api/recruiters/**"
                ).hasRole("RECRUITER")

                // Recruiter creates jobs
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/jobs"
                ).hasRole("RECRUITER")

                // Recruiter updates jobs
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/jobs/**"
                ).hasRole("RECRUITER")

                // Recruiter deletes jobs
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/jobs/**"
                ).hasRole("RECRUITER")

                // Public job browsing
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/jobs",
                    "/api/jobs/page",
                    "/api/jobs/search",
                    "/api/jobs/*"
                ).permitAll()

                // Everything else requires authentication
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of(
                    "GET",
                    "POST",
                    "PUT",
                    "DELETE",
                    "OPTIONS"
                ));

        configuration.setAllowedHeaders(
                List.of("*"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration);

        return source;
    }
}