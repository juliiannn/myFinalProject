package co.edu.iub.myfinalproject.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {

        val bearerSchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("Colegio API")
                    .version("1.0.0")
                    .description("API REST para la gestión de un colegio con autenticación JWT")
            )
            .addSecurityItem(
                SecurityRequirement().addList(bearerSchemeName)
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        bearerSchemeName,
                        SecurityScheme()
                            .name(bearerSchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                    )
            )
    }
}