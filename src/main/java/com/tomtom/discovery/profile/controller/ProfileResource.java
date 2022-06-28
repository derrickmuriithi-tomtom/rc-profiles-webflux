package com.tomtom.discovery.profile.controller;

import com.tomtom.discovery.profile.entity.Profile;
import com.tomtom.discovery.profile.entity.Profile.ProfileState;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@Builder
public class ProfileResource {
    String profileId;
    String name;
    ProfileState state;
    UUID companyId;
    @Nullable
    String description;
    @Nullable
    String sourceMapUri;
    String bbox;
    String sourceMapVersion;
    int rcMapVersion;

    public static ProfileResource getResourceFromModel(Profile profile) {
        return builder()
            .profileId(profile.getProfileId())
            .name(profile.getName())
            .state(profile.getState())
            .companyId(profile.getCompanyId())
            .description(profile.getDescription().orElse(null))
            .bbox(profile.getArea())
            .sourceMapVersion(profile.getSourceMapVersion())
            .rcMapVersion(profile.getRcMapVersion())
            .build();
    }
}
