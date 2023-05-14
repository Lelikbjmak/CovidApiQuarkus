package com.innowise.covidapi.repository;

import com.innowise.covidapi.entity.Country;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CountryRepository implements PanacheRepository<Country> {

    public Optional<Country> findByName(String name) {
        return Optional.ofNullable(find("name", name).firstResult());
    }

}
