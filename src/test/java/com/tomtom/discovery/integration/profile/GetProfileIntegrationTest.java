package com.tomtom.discovery.integration.profile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_CLASS;

import com.tomtom.discovery.DiscoveryReactiveApplication;
import com.tomtom.discovery.company.Company;
import com.tomtom.discovery.company.repository.CompanyRepository;
import com.tomtom.discovery.profile.controller.ProfileResource;
import com.tomtom.discovery.profile.entity.Profile;
import com.tomtom.discovery.profile.repository.ProfileRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@DirtiesContext(classMode = BEFORE_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = DiscoveryReactiveApplication.class)
class GetProfileIntegrationTest {

    protected static final Company COMPANY = Company.builder()
        .id(UUID.fromString("deeaf401-3c69-4945-a4c6-61c0a9c00027"))
        .name("Company")
        .build();
    protected static final Profile NEW_PROFILE = Profile.builder()
        .id(UUID.randomUUID())
        .profileId("ABC-345")
        .companyId(COMPANY.getId())
        .state(Profile.ProfileState.CREATED)
        .rcMapVersion(1)
        .sourceMapVersion("SOURCE_MAP_VERSION_74")
        .build();


    @Autowired
    private WebTestClient testClient;

    @MockBean
    ProfileRepository profileRepository;
    @MockBean
    CompanyRepository companyRepository;

    @Test
    void whenEndpointWithBlockingClientIsCalled_thenThreeTweetsAreReceived() {
        when(companyRepository.findCompanyByName(any())).thenReturn(Mono.just(COMPANY));
        when(profileRepository.findAll()).thenReturn(Flux.just(NEW_PROFILE));

        testClient.get()
            .uri("/external/profiles")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(ProfileResource.class).hasSize(1).contains(ProfileResource.getResourceFromModel(NEW_PROFILE));
    }

}