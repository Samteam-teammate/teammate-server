package com.samteam.teammate.domain.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.samteam.teammate.domain.auth.dto.AuthLoginRequest;
import com.samteam.teammate.domain.auth.dto.AuthRequest;
import com.samteam.teammate.domain.auth.dto.AuthResponse;
import com.samteam.teammate.domain.member.dto.MemberLoginResponse;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
	private static final String AUTH_ENDPOINT = "/auth";
	private final WebClient sejongUnivAuthWebClient;

	private final AuthTokenProvider authTokenProvider;
	private final MemberRepository memberRepository;

	public void authenticateSejong(AuthLoginRequest request) {
		AuthRequest requestBody = AuthRequest.builder()
			.id(request.studentId())
			.pw(request.password())
			.method("Manual")
			.build();

		Mono<AuthResponse> response = sejongUnivAuthWebClient.post()
			.uri(AUTH_ENDPOINT)
			.body(BodyInserters.fromValue(requestBody))
			.retrieve()
			.onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
				clientResponse -> clientResponse.bodyToMono(String.class)
					.flatMap(errorBody -> Mono.error(new RuntimeException("세종대 인증 중 에러 발생" + errorBody))))
			.bodyToMono(AuthResponse.class)
			.doOnError(e -> log.error("세종대 인증 중 예외 발생", e));

		AuthResponse authResponse = response.block();

		if (authResponse == null || !Boolean.TRUE.equals(authResponse.result().isAuth())) {
			throw new RuntimeException("세종대 인증 중 예외 발생");
		}
	}

	public MemberLoginResponse buildLoginResponseByStudentId(Long studentId) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));
		return MemberLoginResponse.from(member);
	}

	public String genAccessToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genAccessToken(memberResponse.id());
	}

	public String genRefreshToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genRefreshToken(memberResponse.id());
	}
}
