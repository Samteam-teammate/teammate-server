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

    public MemberLoginResponse buildLoginResponseByStudentId(Long studentId) {
        Member member = memberRepository.findByStudentId(studentId)
            .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));
        return MemberLoginResponse.from(member);
    }

    /** 학번으로 memberId만 해석해야 할 때 사용 가능 */
    public Long resolveMemberIdByStudentId(Long studentId) {
        return memberRepository.findByStudentId(studentId)
            .map(Member::getId)
            .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));
    }

	public String genAccessToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genAccessToken(memberResponse.id());
	}

	public String genRefreshToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genRefreshToken(memberResponse.id());
	}
}
