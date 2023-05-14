package com.innowise.covidapi.validator;

import com.innowise.covidapi.annotation.ValidCountry;
import com.innowise.covidapi.repository.CountryRepository;
import com.innowise.covidapi.util.ApplicationConstant;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@RequiredArgsConstructor
public class CountryValidator implements ConstraintValidator<ValidCountry, List<String>> {

    private final CountryRepository countryRepository;

    @Override
    public boolean isValid(List<String> countryList, ConstraintValidatorContext constraintValidatorContext) {

        if (countryList.isEmpty()) {
            buildConstraintViolation(constraintValidatorContext, "No country provided.");
            return false;
        }

        boolean validCountries = countryList.stream()
                .allMatch(country -> countryRepository.findByName(country).isPresent());

        if (!validCountries) {
            buildConstraintViolation(constraintValidatorContext, ApplicationConstant.Message.NOT_VALID_COUNTRY_MESSAGE);
            return false;
        }

        return true;
    }

    private void buildConstraintViolation(ConstraintValidatorContext context, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }
}
