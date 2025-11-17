package com.samteam.teammate.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.samteam.teammate.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByStudentId(Long studentId);
	Boolean existsByStudentId(Long studentId);
}
