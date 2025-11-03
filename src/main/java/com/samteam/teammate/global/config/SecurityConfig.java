package com.samteam.teammate.global.config;

import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.global.security.JWTAuthenticationFilter;
import com.samteam.teammate.global.security.JwtAuthEntryPoint;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;


    public SecurityConfig(AuthTokenProvider authTokenProvider, MemberRepository memberRepository) {
        this.authTokenProvider = authTokenProvider;
        this.memberRepository = memberRepository;
    }

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtFilter = new JWTAuthenticationFilter(authTokenProvider, memberRepository);
        http
            .cors(c -> c.configurationSource(
                corsConfigurationSource()))
			.csrf(AbstractHttpConfigurer::disable)
            .headers(h -> h
                .frameOptions(f -> f.disable())
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            )
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(eh -> eh.authenticationEntryPoint(new JwtAuthEntryPoint()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/health",
                    "/docs/**",
                    "/actuator/**",
                    "/h2-console/**",
                    "/auth/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// CORS 설정을 분리해 둔 Bean
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("http://localhost:3000"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.addExposedHeader("Authorization");

		// 모든 엔드포인트에 대해 CORS 설정 적용
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	// 패스워드 암호화를 위한 Bean
	@Bean
	public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
	}
}
