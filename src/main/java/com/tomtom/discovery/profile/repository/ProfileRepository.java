
package com.tomtom.discovery.profile.repository;

import com.tomtom.discovery.profile.entity.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfileRepository extends ReactiveCrudRepository<Profile,String> {
    Mono<Profile> findByProfileId(String profileId);
}
