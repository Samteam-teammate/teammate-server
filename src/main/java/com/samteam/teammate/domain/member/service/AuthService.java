package com.samteam.teammate.domain.member.service;

import org.springframework.stereotype.Service;

import com.samteam.teammate.domain.member.dto.MemberLoginResponse;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthTokenProvider authTokenProvider;
	private final MemberRepository memberRepository;

    public MemberLoginResponse buildLoginResponseByStudentId(Long studentId) {
        Member member = memberRepository.findByStudentId(studentId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberLoginResponse.from(member);
    }

	public String genAccessToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genAccessToken(memberResponse.id());
	}

	public String genRefreshToken(MemberLoginResponse memberResponse) {
		return authTokenProvider.genRefreshToken(memberResponse.id());
	}
}
