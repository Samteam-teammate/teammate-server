package com.samteam.teammate.domain.scrap.repository;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.scrap.entity.ProfileScrap;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileScrapRepository extends JpaRepository<ProfileScrap, Long> {

    boolean existsByMemberAndProfile(Member member, Profile profile);

    Optional<ProfileScrap> findByMemberAndProfile(Member member, Profile profile);

    Page<ProfileScrap> findByMember(Member member, Pageable pageable);

}
