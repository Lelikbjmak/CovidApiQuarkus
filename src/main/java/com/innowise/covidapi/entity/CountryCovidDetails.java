package com.innowise.covidapi.entity;

import com.innowise.covidapi.entity.id.CountryCovidDetailsId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Entity
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "country_covid_details")
public class CountryCovidDetails {

    @EmbeddedId
    private CountryCovidDetailsId id;

    private long cases;

    private long totalCases;

}
