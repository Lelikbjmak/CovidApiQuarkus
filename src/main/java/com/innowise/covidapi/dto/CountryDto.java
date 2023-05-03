package com.innowise.covidapi.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonbPropertyOrder({"name", "slug", "code"})
public class CountryDto {

    @JsonAlias("Country")
    private String name;

    @JsonAlias("Slug")
    private String slug;

    @JsonAlias("ISO2")
    private String code;
}
