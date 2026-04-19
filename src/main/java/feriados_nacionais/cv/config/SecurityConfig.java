package feriados_nacionais.cv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Allow public access to Swagger and API docs
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/api-docs/**").permitAll()
                // Allow public access to actuator endpoints
                .requestMatchers("/actuator/**").permitAll()
                // Allow public access to your API endpoints (adjust as needed)
                .requestMatchers("/api/**").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API development
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.deny())
            );
        
        return http.build();
    }
}