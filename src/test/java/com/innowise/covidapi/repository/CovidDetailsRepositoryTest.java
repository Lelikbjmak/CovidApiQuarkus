package com.innowise.covidapi.repository;

import com.innowise.covidapi.config.TestResourceConfig;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.util.CovidDetailsIdProvider;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@QuarkusTest
@TestProfile(TestResourceConfig.class)
class CovidDetailsRepositoryTest {

    @Inject
    private CovidDetailsRepository covidDetailsRepository;

    @Inject
    private EntityManager entityManager;

    @BeforeEach
    @Transactional
    void persistData() {
        entityManager.createNativeQuery("INSERT INTO country_covid_details VALUES('2021-03-14', 35, 1998, 'Belarus')")
                .executeUpdate();
    }

    @AfterEach
    @Transactional
    void truncateData() {
        entityManager.createNativeQuery("DELETE FROM country_covid_details")
                .executeUpdate();
    }

    @Test
    void findById() {
        final CountryCovidDetailsId id = CovidDetailsIdProvider
                .getCovidDetailsId("Belarus", LocalDate.of(2021, 3, 14));
        Assertions.assertTrue(covidDetailsRepository.findById(id).isPresent());
    }

    @Test
    @Transactional
    void save() {
        final CountryCovidDetailsId id = CovidDetailsIdProvider
                .getCovidDetailsId("Japan", LocalDate.of(2021, 3, 14));

        Assertions.assertTrue(covidDetailsRepository.findById(id).isEmpty());

        covidDetailsRepository.persist(new CountryCovidDetails(id, 298, 1657));

        Assertions.assertTrue(covidDetailsRepository.findById(id).isPresent());
    }
}