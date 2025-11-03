package com.samteam.teammate.global.security;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public JWTAuthenticationFilter(AuthTokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String prefix = "Bearer ";

        if (StringUtils.hasText(header) && header.startsWith(prefix)) {
            String token = header.substring(prefix.length()).trim();

            if (tokenProvider.isValidToken(token)) {
                String subject = tokenProvider.getSubject(token); // memberId as String
                Long memberId = Long.parseLong(subject);

                // DB에서 studentId까지 채워 넣고 싶으면 조회(선택)
                Member member = memberRepository.findById(memberId).orElse(null);
                MemberPrincipal principal = (member != null)
                    ? new MemberPrincipal(member.getId(), member.getStudentId())
                    : new MemberPrincipal(memberId, null);

                var auth = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    // 권한이 있으면 넣고, 지금 단계에선 비워둬도 OK
                    AuthorityUtils.NO_AUTHORITIES
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}