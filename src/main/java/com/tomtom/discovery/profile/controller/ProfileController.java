package com.tomtom.discovery.profile.controller;

import com.tomtom.discovery.aggregations.ProfileAggregationService;
import com.tomtom.discovery.profile.entity.Profile;
import com.tomtom.discovery.profile.service.ProfileService;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/external")
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileAggregationService profileAggregationService;

    @Autowired
    public ProfileController(ProfileService profileService, ProfileAggregationService profileAggregationService) {
        this.profileService = profileService;
        this.profileAggregationService = profileAggregationService;
    }

    @GetMapping("/profiles")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    public Flux<ProfileResource> getProfiles(Authentication authentication) {
        return profileService.getAuthorizedAvailableProfiles()
            .map(ProfileResource::getResourceFromModel);
    }

    @GetMapping(value = "/stream/profiles",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    public Flux<ProfileResource> getProfilesStream(Authentication authentication) {
        return profileService.getAuthorizedAvailableProfiles()
            .delayElements(Duration.of(1000L, ChronoUnit.MILLIS))
            .map(ProfileResource::getResourceFromModel);
    }

    @GetMapping("/profiles/{id}")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.GET)
    public Mono<ProfileResource> getProfile(@PathVariable String id, Authentication authentication) {
        return profileService.getProfileById(id).
            flatMap(profile -> profileService.isProfileAvailable(profile)
                .map(profile1 -> ProfileResource.getResourceFromModel(profile)));
    }

    @PostMapping("/profile")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public Mono<ResponseEntity<Void>> createProfile(@RequestBody @Valid ProfilePostRequest requestBody) {
        var profile = Profile.builder()
            .profileId(requestBody.getProfileId())
            .name(requestBody.getName())
            .sourceMapVersion(requestBody.getSourceMapVersion())
            .area(requestBody.getBbox())
            .description(requestBody.getDescription())
            .build();

        return profileAggregationService.createProfile(profile, requestBody.getCompanyName())
            .flatMap(profile1 -> Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }
}