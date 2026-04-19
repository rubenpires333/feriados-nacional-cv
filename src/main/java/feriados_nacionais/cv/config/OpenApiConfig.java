package feriados_nacionais.cv.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8083}")
    private String serverPort;
    
    private final Environment environment;
    
    public OpenApiConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        
        // Servidor de produção sempre primeiro
        servers.add(new Server()
                .url("https://public.api.konektadev.cv/feriados-nacionais")
                .description("🌐 Servidor de Produção"));
        
        // Adicionar servidores locais apenas se não estiver em produção
        if (!isProductionProfile()) {
            servers.add(new Server()
                    .url("http://localhost:" + serverPort + "/feriados-nacionais")
                    .description("🏠 Servidor Local"));
            servers.add(new Server()
                    .url("http://0.0.0.0:" + serverPort + "/feriados-nacionais")
                    .description("🐳 Servidor Docker"));
        }
        
        // Servidor relativo (sempre disponível)
        servers.add(new Server()
                .url("/feriados-nacionais")
                .description("📍 Servidor Atual"));
        
        return new OpenAPI()
                .info(new Info()
                        .title("🇨🇻 Feriados Nacionais CV API")
                        .description("API REST para consulta de feriados nacionais de Cabo Verde. " +
                                   "Inclui feriados nacionais, municipais, tradicionais e móveis (baseados na Páscoa).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Feriados Nacionales de Cabo Verde - API REST")
                                .email("rubenpires333@gmail.com")
                                .url("https://public.api.konektadev.cv/feriados-nacionais"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(servers)
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticação. Use o token do arquivo .env")));
    }
    
    private boolean isProductionProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("prod".equals(profile) || "production".equals(profile)) {
                return true;
            }
        }
        return false;
    }
}