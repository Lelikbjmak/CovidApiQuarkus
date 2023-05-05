CREATE TABLE country_covid_details (
    country VARCHAR(60),
    date DATE,
    cases BIGINT,
    totalCases BIGINT,
    PRIMARY KEY(country, date)
)
GO

