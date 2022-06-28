package com.tomtom.discovery.company;

import java.util.Objects;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table("company")
public class Company {
    @NonNull
    @Id
    UUID id;
    @NonNull
    String name;

    public static CompanyBuilder builder() {
        return new CompanyBuilder() {
            @Override
            public Company build() {
                prebuild();
                return super.build();
            }
        };
    }

    public static class CompanyBuilder {
        void prebuild() {
            if (Objects.isNull(id)) {
                id = UUID.randomUUID();
            }
        }
    }
}