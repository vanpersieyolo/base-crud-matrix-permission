package org.example.basespringbootproject.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI / Swagger config that exposes a JWT "Authorize" button on Swagger UI.
 * <p>
 * Use the annotation-based SecurityScheme for docs, and create an OpenAPI bean using
 * fully-qualified model classes to avoid import name collisions.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Base Spring Boot Project API", version = "v1"),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Authorization header using the Bearer scheme. Example: 'Bearer {token}'",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        // use fully-qualified model classes to avoid name clashes with annotations
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Base Spring Boot Project API")
                        .version("v1.0")
                        .description("API docs for authentication & authorization")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Support")
                                .email("support@example.com")
                                .url("https://example.com"))
                        .license(new io.swagger.v3.oas.models.info.License().name("Apache-2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                // add the security requirement (model) referencing the scheme name declared above
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("bearerAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .name("Authorization")
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                        )
                );
    }
}
