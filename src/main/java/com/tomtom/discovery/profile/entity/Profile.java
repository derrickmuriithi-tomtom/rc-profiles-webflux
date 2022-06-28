package com.tomtom.discovery.profile.entity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("profile")
public class Profile {
    private static final Map<ProfileState, List<ProfileState>> ALLOWED_STATE_TRANSITIONS =
        Map.of(ProfileState.CREATED, List.of(ProfileState.COMPILING_INITIAL_RC_MAP, ProfileState.INITIAL_COMPILATION_FAILED),
            ProfileState.COMPILING_INITIAL_RC_MAP, List.of(ProfileState.ACTIVE, ProfileState.INITIAL_COMPILATION_FAILED),
            ProfileState.INITIAL_COMPILATION_FAILED, List.of(ProfileState.COMPILING_INITIAL_RC_MAP, ProfileState.INITIAL_COMPILATION_FAILED),
            ProfileState.ACTIVE, List.of(ProfileState.UPDATING, ProfileState.INACTIVE, ProfileState.INITIAL_COMPILATION_FAILED, ProfileState.APPLYING_RULESET),
            ProfileState.UPDATING, List.of(ProfileState.ACTIVE, ProfileState.PENDING_REWORK),
            ProfileState.INACTIVE, List.of(ProfileState.INITIAL_COMPILATION_FAILED),
            ProfileState.APPLYING_RULESET, List.of(ProfileState.ACTIVE),
            ProfileState.PENDING_REWORK, List.of(ProfileState.ACTIVE)
        );

    public enum ProfileState {
        CREATED,
        COMPILING_INITIAL_RC_MAP,
        INITIAL_COMPILATION_FAILED,
        ACTIVE,
        UPDATING,
        INACTIVE,
        APPLYING_RULESET,
        PENDING_REWORK
    }
    @Id
    UUID id;

    @Column("profile_id")
    String profileId;

    @Column("company_id")
    UUID companyId;
    String name;
    @Nullable
    String description;
    String area;
    ProfileState state;
    @Nullable
    @Column("source_map_version")
    String sourceMapVersion;

    @Column("rc_map_version")
    int rcMapVersion;

    public boolean isActive() {
        return (state == ProfileState.ACTIVE);
    }

    public boolean isInReworkState() {
        return (state == ProfileState.PENDING_REWORK);
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public static void validateStateTransition(ProfileState fromState, ProfileState toState) {
        Assert.isTrue(ALLOWED_STATE_TRANSITIONS.get(fromState).contains(toState),
            String.format("Profile does not allow a transition from %s to %s.", fromState, toState));
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id='" + id + '\'' +
            ", companyId=" + companyId +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", area='" + area + '\'' +
            ", state=" + state +
            ", sourceMapVersion='" + sourceMapVersion + '\'' +
            ", rcMapVersion=" + rcMapVersion +
            '}';
    }
}

