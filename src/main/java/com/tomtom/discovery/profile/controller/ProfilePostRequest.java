package com.tomtom.discovery.profile.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

@Value
@Builder
public class ProfilePostRequest {
    @NotEmpty
    @Size(min = 1, max = 64, message = "Profile ID must have minimum {min}, maximum {max} characters.")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Invalid profile ID")
    String profileId;

    @NotEmpty
    @Size(min = 3, max = 50, message = "profile name must have minimum {min}, maximum {max} characters.")
    String name;

    @NotEmpty
    @Size(min = 3, max = 50, message = "company name must have minimum {min}, maximum {max} characters.")
    String companyName;

    @NotEmpty
    String bbox;

    @NotEmpty
    String sourceMapVersion;

    @Nullable
    @Size(min = 3, max = 100, message = "profile description must have minimum {min}, maximum {max} characters.")
    String description;
}
