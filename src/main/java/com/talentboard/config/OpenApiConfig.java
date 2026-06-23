package com.talentboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI talentBoardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TalentBoard API")
                        .description("Recruitment process management API")
                        .version("1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH))
                .components(new Components().addSecuritySchemes(BASIC_AUTH,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
