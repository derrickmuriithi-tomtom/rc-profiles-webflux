package com.tomtom.discovery.profile.service;

import com.tomtom.discovery.profile.entity.Profile;
import com.tomtom.discovery.profile.entity.Profile.ProfileState;
import com.tomtom.discovery.exceptions.EntityNotFoundException;
import com.tomtom.discovery.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Mono<Profile> getProfileById(String id){
        return profileRepository.findByProfileId(id).
            switchIfEmpty(Mono.error(new EntityNotFoundException("Profile not found.")));
    }

    public Mono<Boolean> isProfileAvailable(Profile profile){
        return Mono.just((profile.getState() == ProfileState.ACTIVE)
            || (profile.getState() == ProfileState.UPDATING)
            || (profile.getState() == ProfileState.APPLYING_RULESET)
            || (profile.getState() == ProfileState.PENDING_REWORK))
            .onErrorMap(error -> new Exception("Profile is not in ACTIVE, UPDATING, APPLYING_RULESET or PENDING_REWORK state."));
    }

    public Flux<Profile> getAuthorizedAvailableProfiles(){
        return this.profileRepository.findAll().
            onErrorMap(error -> new Exception("Profiles not found."));
    }

    public Mono<Profile> saveProfile(Profile profile){
        return this.profileRepository.save(profile);
    }

}
