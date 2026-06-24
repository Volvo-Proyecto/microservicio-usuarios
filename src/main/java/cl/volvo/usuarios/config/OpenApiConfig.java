package cl.volvo.usuarios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Usuarios - Volvo")
                        .version("1.0.0")
                        .description("Microservicio encargado de la gestión, registro y administración de los usuarios del sistema Volvo.")
                        .contact(new Contact()
                                .name("Volvo")
                                .email("soporte@volvo.cl")));
    }
}