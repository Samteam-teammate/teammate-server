package com.samteam.teammate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("TeamMate API")
			.description("3팀 팀메이트 백엔드 API 명세서");

		String securitySchemeName = "bearerAuth";

		SecurityScheme bearerAuth = new SecurityScheme()
			.name(securitySchemeName)
			.type(SecurityScheme.Type.HTTP) // 인증 타입: HTTP
			.scheme("bearer")               // 스킴: Bearer
			.bearerFormat("JWT");           // 포맷: JWT

		SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

		return new OpenAPI()
			.info(info)
			.components(new Components().addSecuritySchemes(securitySchemeName, bearerAuth))
			.addSecurityItem(securityRequirement);
	}
}
