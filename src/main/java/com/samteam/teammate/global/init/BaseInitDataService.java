package com.samteam.teammate.global.init;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BaseInitDataService {
	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public void initData() {
		// member 데이터가 없는 경우에만 실행
		if (memberRepository.count()>0) {
			return;
		}

		long studentId = 20230001L;
		List<String> name = List.of("김민수", "이수현", "박정우", "최예린", "한지훈", "강민서", "이도윤", "서지우", "정하늘", "윤서연");
		List<Major> major = List.of(Major.CS, Major.SW, Major.CS, Major.AI, Major.CS, Major.SW, Major.AI, Major.CS, Major.CS, Major.AI);
		List<Integer> grade = List.of(4, 4, 3, 4, 3, 4, 4, 4, 4, 4);
		List<List<TechType>> tech = List.of(
			List.of(TechType.C, TechType.PYTHON, TechType.JAVA, TechType.REACT),
			List.of(TechType.C, TechType.PYTHON, TechType.NODEJS, TechType.AI, TechType.UNITY, TechType.UNREAL),
			List.of(TechType.C, TechType.PYTHON, TechType.JAVA),
			List.of(TechType.C, TechType.AI, TechType.UNITY, TechType.UNREAL),
			List.of(TechType.C, TechType.PYTHON, TechType.JAVA, TechType.SPRING, TechType.AI),
			List.of(TechType.NODEJS, TechType.AI, TechType.UNITY, TechType.UNREAL),
			List.of(TechType.C, TechType.PYTHON, TechType.JAVA, TechType.REACT, TechType.FLUTTER, TechType.ANDROID),
			List.of(TechType.SPRING, TechType.NODEJS),
			List.of(TechType.NODEJS, TechType.AI),
			List.of(TechType.UNITY, TechType.UNREAL)
		);
		List<FieldType> applicationField = List.of(FieldType.PM, FieldType.FE, FieldType.BE, FieldType.VR, FieldType.DESIGN, FieldType.ETC, FieldType.PM, FieldType.FE, FieldType.BE, FieldType.VR, FieldType.DESIGN);
		List<String> contactInfo = List.of("Email", "KakaoTalk", "Email", "Email", "Instagram", "KakaoTalk", "KakaoTalk", "Email", "Instagram", "Email");
		List<Boolean> visibility = List.of(true, false, true, true, true, true, true, false, true, true);

		for (int i=0; i<10; i++) {
			Member member = Member.builder()
				.studentId(studentId)
				.build();

			Profile profile = Profile.builder()
				.member(member)
				.name(name.get(i))
				.major(major.get(i))
				.grade(grade.get(i))
				.teckStack(tech.get(i))
				.applicationField(applicationField.get(i))
				.contactInfo(contactInfo.get(i))
				.simpleInfo("simple info " + i)
				.additionalInfo("additional info " + i)
				.visibility(visibility.get(i))
				.build();

			memberRepository.save(member);
			profileRepository.save(profile);

			studentId++;
		}
	}
}
