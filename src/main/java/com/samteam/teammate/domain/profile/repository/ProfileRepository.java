package com.samteam.teammate.domain.profile.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
	@Query("SELECT p FROM Profile p " +
		"WHERE (:major IS NULL OR p.major = :major) " +
		"AND (:applicationField IS NULL OR p.applicationField = :applicationField)" +
		"AND (p.visibility IS TRUE)")
	Page<Profile> findAllByVisibilityTrueAndConditions(
		@Param("major") Major major,
		@Param("applicationField") FieldType field,
		Pageable pageable);
}
