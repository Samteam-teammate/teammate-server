package com.samteam.teammate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("TeamMate API")
			.description("3팀 팀메이트 백엔드 API 명세서");

		return new OpenAPI()
			.info(info);
	}
}
