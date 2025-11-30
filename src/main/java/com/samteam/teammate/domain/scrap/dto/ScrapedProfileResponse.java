package com.samteam.teammate.domain.scrap.dto;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.scrap.entity.ProfileScrap;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;
import java.util.List;

public record ScrapedProfileResponse(
    Long profileId,
    String name,
    Major major,
    Integer grade,
    List<TechType> techStack,
    FieldType applicationField,
    String simpleInfo,
    String contactInfo,
    Boolean scraped
) {
    public static ScrapedProfileResponse from(ProfileScrap scrap) {
        Profile p = scrap.getProfile();
        return new ScrapedProfileResponse(
            p.getId(),
            p.getName(),
            p.getMajor(),
            p.getGrade(),
            p.getTeckStack(),
            p.getApplicationField(),
            p.getSimpleInfo(),
            p.getContactInfo(),
            true
        );
    }
}