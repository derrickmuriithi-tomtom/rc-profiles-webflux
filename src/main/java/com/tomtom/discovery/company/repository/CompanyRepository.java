package com.tomtom.discovery.company.repository;

import com.tomtom.discovery.company.Company;

import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CompanyRepository extends ReactiveCrudRepository<Company, UUID> {
    Mono<Company> findCompanyByName(String name);
}