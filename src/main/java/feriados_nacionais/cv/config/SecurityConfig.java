package feriados_nacionais.cv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Allow public access to Swagger and API docs
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/swagger-ui.html", 
                    "/v3/api-docs/**", 
                    "/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                // Allow public access to actuator endpoints
                .requestMatchers("/actuator/**").permitAll()
                // Allow public access to your API endpoints (adjust as needed)
                .requestMatchers("/api/**", "/feriados/**", "/localidades/**").permitAll()
                // Allow OPTIONS requests for CORS
                .requestMatchers("OPTIONS", "/**").permitAll()
                // Require authentication for all other requests
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API development
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
                .contentTypeOptions(contentTypeOptions -> contentTypeOptions.and())
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig.disable())
            );
        
        return http.build();
    }
    
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.Arrays.asList(
            "https://public.api.konektadev.cv",
            "https://*.konektadev.cv",
            "http://localhost:*",
            "http://127.0.0.1:*"
        ));
        configuration.setAllowedMethods(java.util.Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"
        ));
        configuration.setAllowedHeaders(java.util.List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = 
            new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}