package com.samteam.teammate.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.samteam.teammate.domain.member.dto.MemberLoginResponse;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthTokenProvider authTokenProvider;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberLoginResponse login(Long studentId, String password) {
		Member member = memberRepository.findByStudentId(studentId)
			.orElseThrow(() -> new RuntimeException("회원가입이 필요합니다"));

		validateUser(password, member);

		return MemberLoginResponse.from(member);
	}

	private void validateUser(String password, Member member) {
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다");
		}
	}

	public String genAccessToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genAccessToken(memberResponse.id());
	}

	public String genRefreshToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genRefreshToken(memberResponse.id());
	}
}
