package com.innowise.covidapi.repository;

import com.innowise.covidapi.entity.Country;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CountryRepository implements PanacheRepository<Country> {

    public Optional<Country> findByName(String name) {
        return Optional.of(find("name", name).firstResult());
    }

    public Optional<Country> findById(String slug) {
        return Optional.of(find("slug", slug).firstResult());
    }

}
