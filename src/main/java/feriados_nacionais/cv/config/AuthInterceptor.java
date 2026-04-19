package feriados_nacionais.cv.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final Environment environment;

    public AuthInterceptor(Environment environment) {
        this.environment = environment;
    }

    private String getApiToken() {
        // Tentar obter o token de várias fontes
        String token = environment.getProperty("API_TOKEN");
        if (token == null) {
            token = environment.getProperty("api.token");
        }
        if (token == null) {
            token = System.getProperty("API_TOKEN");
        }
        if (token == null) {
            token = System.getenv("API_TOKEN");
        }
        // Token padrão como fallback
        if (token == null) {
            token = "cv-feriados-2024-secure-token-a1b2c3d4e5f6g7h8i9j0";
        }
        return token;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // Permitir OPTIONS para CORS
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // Permitir acesso ao Swagger e Actuator sem token
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui") || 
            requestURI.startsWith("/v3/api-docs") || 
            requestURI.startsWith("/actuator")) {
            return true;
        }

        // Verificar token no header Authorization
        String authHeader = request.getHeader("Authorization");
        String token = null;
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        
        // Verificar token como query parameter (alternativa)
        if (token == null) {
            token = request.getParameter("token");
        }

        // Validar token
        String apiToken = getApiToken();
        if (token == null || !apiToken.equals(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Token de autenticação inválido ou ausente\",\"codigo\":401}");
            return false;
        }

        return true;
    }
}