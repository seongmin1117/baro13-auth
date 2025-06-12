package com.baro13.auth.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .components(
            new Components()
                .addSecuritySchemes("BearerAuth", createBearerScheme())
        )
        .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
        .info(apiInfo());
  }

  private SecurityScheme createBearerScheme() {
    return new SecurityScheme()
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
  }

  private Info apiInfo() {
    return new Info()
        .title("Baro13 Auth API")
        .description("바로인턴 13기 직무 과제 API 명세서")
        .version("1.0");
  }
}
