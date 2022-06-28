package com.tomtom.discovery.aggregations;

import com.tomtom.discovery.company.service.CompanyService;
import com.tomtom.discovery.profile.entity.Profile;
import com.tomtom.discovery.profile.entity.Profile.ProfileState;
import com.tomtom.discovery.profile.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProfileAggregationService {
    private final CompanyService companyService;
    private final ProfileService profileService;

    @Autowired
    public ProfileAggregationService(CompanyService companyService, ProfileService profileService) {
        this.companyService = companyService;
        this.profileService = profileService;
    }

    public Mono<Profile> createProfile(Profile profile, String companyName){
        return companyService.getCompanyByName(companyName)
            .flatMap(company -> {
                var profileToCreate = profile.toBuilder()
                    .companyId(company.getId())
                    .state(ProfileState.CREATED)
                    .build();

                return this.profileService.saveProfile(profileToCreate);
        });
    }
}
