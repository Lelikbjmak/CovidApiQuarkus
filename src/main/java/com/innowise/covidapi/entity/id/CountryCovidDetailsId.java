package com.innowise.covidapi.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@ToString
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CountryCovidDetailsId implements Serializable {

    private String country;

    @Column(columnDefinition = "date")
    private LocalDate date;

}
