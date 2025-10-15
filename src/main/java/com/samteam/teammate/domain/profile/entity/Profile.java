package com.samteam.teammate.domain.profile.entity;

import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.global.util.BaseTimeEntity;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "profile")
public class Profile extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "profile_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(name = "major")
	private Major major;

	@Column(name = "grade")
	private Integer grade;

	@Enumerated(EnumType.STRING)
	@Column(name = "application_field")
	private FieldType applicationField;

	@Column(name = "contact_info", columnDefinition = "TEXT")
	private String contactInfo;

	@Column(name = "additional_info", columnDefinition = "TEXT")
	private String additionalInfo;

	@Column(name = "visibility", nullable = false)
	private Boolean visibility;

	public void update(MemberProfileUpdateRequest dto) {
		if (dto.name() != null) {
			this.name = dto.name();
		}
		if (dto.major() != null) {
			this.major = dto.major();
		}
		if (dto.grade() != null) {
			this.grade = dto.grade();
		}
		if (dto.applicationField() != null) {
			this.applicationField = dto.applicationField();
		}
		if (dto.contactInfo() != null) {
			this.contactInfo = dto.contactInfo();
		}
		if (dto.additionalInfo() != null) {
			this.additionalInfo = dto.additionalInfo();
		}
		if (dto.visibility() != null) {
			this.visibility = dto.visibility();
		}
	}
}
