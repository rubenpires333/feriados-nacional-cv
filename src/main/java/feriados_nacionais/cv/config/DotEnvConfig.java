package feriados_nacionais.cv.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class DotEnvConfig {

    @PostConstruct
    public void loadEnvVariables() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./")
                    .filename(".env")
                    .ignoreIfMissing()
                    .load();

            // Carregar todas as variáveis do .env para as propriedades do sistema
            dotenv.entries().forEach(entry -> {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Só define se a variável ainda não existe no sistema
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            });

            System.out.println("✅ Arquivo .env carregado com sucesso!");
            
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar arquivo .env: " + e.getMessage());
            System.err.println("Continuando com configurações padrão...");
        }
    }
}