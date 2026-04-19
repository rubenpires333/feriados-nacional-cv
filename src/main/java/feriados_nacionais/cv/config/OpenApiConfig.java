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

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8083}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🇨🇻 Feriados Nacionais CV API")
                        .description("API REST para consulta de feriados nacionais de Cabo Verde. " +
                                   "Inclui feriados nacionais, municipais, tradicionais e móveis (baseados na Páscoa).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Feriados Nacionales de Cabo Verde - API REST")
                                .email("rubenpires333@gmail.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("https://public.api.konektadev.cv/feriados-nacionais")
                                .description("Servidor de Produção"),
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Servidor Local"),
                        new Server()
                                .url("http://0.0.0.0:" + serverPort)
                                .description("Servidor Docker"),
                        new Server()
                                .url("/")
                                .description("Servidor Atual")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT para autenticação. Use o token do arquivo .env")));
    }
}