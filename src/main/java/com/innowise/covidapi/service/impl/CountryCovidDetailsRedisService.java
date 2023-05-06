package com.innowise.covidapi.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.covidapi.dto.CountryCovidDetailsDto;
import com.innowise.covidapi.entity.CountryCovidDetails;
import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import com.innowise.covidapi.mapper.CovidDetailsMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class CountryCovidDetailsRedisService {

    @ConfigProperty(name = "jedis.host")
    private String host;

    @ConfigProperty(name = "jedis.port")
    private int port;

    private Jedis connection;

    private final ObjectMapper objectMapper;

    private final CovidDetailsMapper covidDetailsMapper;

    @PostConstruct
    public void init() {
        connection = new Jedis(host, port);
        connection.select(2);
    }

    @SneakyThrows
    public void save(CountryCovidDetails countryCovidDetails) {
        String key = countryCovidDetails.getId().getCountry() + " " + countryCovidDetails.getId().getDate();
        connection.set(key, objectMapper.writeValueAsString(countryCovidDetails));
        connection.expire(key, 60 * 60 * 3); // 3 hours
    }

    @SneakyThrows
    public Optional<CountryCovidDetailsDto> findById(CountryCovidDetailsId id) {

        String jsonCovidDetails = connection.get(id.getCountry() + " " + id.getDate());

        return (jsonCovidDetails == null) ?
                Optional.empty() : Optional.of(
                covidDetailsMapper.mapToDto(
                        objectMapper.readValue(jsonCovidDetails, CountryCovidDetails.class)));
    }

    public List<CountryCovidDetailsDto> findByIdList(List<CountryCovidDetailsId> idList) {
        return idList.stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }
}
