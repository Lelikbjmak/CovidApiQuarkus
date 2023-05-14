package com.innowise.covidapi.config;

import io.quarkus.test.junit.QuarkusTestProfile;

public class TestResourceConfig implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "test";
    }
}
