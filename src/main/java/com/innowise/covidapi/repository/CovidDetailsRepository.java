package com.innowise.covidapi.repository;

import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class CovidDetailsRepository implements PanacheRepository<CountryCovidDetails> {

    public Optional<CountryCovidDetails> findById(CountryCovidDetailsId id) {
        return Optional.ofNullable(find("id.country = ?1 AND id.date = ?2",
                id.getCountry(), id.getDate()).firstResult());
    }
}
