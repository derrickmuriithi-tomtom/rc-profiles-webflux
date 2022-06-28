package com.tomtom.discovery.company.service;

import com.tomtom.discovery.company.repository.CompanyRepository;
import com.tomtom.discovery.company.Company;
import com.tomtom.discovery.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Mono<Company> getCompanyByName(String name){
        return companyRepository.findCompanyByName(name).
            switchIfEmpty(Mono.error(new EntityNotFoundException("Company not found.")));
    }

}
