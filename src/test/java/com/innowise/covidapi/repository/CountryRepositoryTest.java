package com.innowise.covidapi.repository;

import com.innowise.covidapi.config.TestResourceConfig;
import com.innowise.covidapi.entity.Country;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestProfile(TestResourceConfig.class)
class CountryRepositoryTest {

    @Inject
    private CountryRepository countryRepository;

    @Test
    void findByName() {
        Assertions.assertTrue(countryRepository.findByName("Japan").isPresent());
        Assertions.assertTrue(countryRepository.findByName("Belarus").isPresent());
    }

    @Test
    @Transactional
    void save() {
        final String countryName = "JavaCountry";

        Assertions.assertFalse(countryRepository.findByName(countryName).isPresent());

        countryRepository.persist(new Country("javacountry", countryName, "JC"));

        Assertions.assertTrue(countryRepository.findByName(countryName).isPresent());
    }
}