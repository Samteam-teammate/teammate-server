package com.samteam.teammate.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
	private static final String SEJONG_UNIV_AUTH_URL = "https://auth.imsejong.com";

	@Bean
	public WebClient sejongUnivAuthWebClient() {
		return WebClient.builder()
			.baseUrl(SEJONG_UNIV_AUTH_URL)
			.defaultHeader("Content_Type", "application/json")
			.build();
	}
}
