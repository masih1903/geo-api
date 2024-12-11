package app.daos;

import app.dtos.CountryDTO;
import app.entities.Country;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CountryDTOTest {

    @Test
    void testFlagUrlMapping() {
        // Create a sample Country entity
        Country country = new Country("USA", "United States of America", "Dollar", "$", "North America", 331449281, List.of("Washington D.C."), "right", List.of("USA"), Map.of("en", "English"), "http://example.com/flags/usa.png");

        // Create the corresponding CountryDTO
        CountryDTO countryDTO = new CountryDTO(country);

        // Check if the flag URL is correctly mapped
        assertNotNull(countryDTO.getFlagUrl());
        assertEquals("http://example.com/flags/usa.png", countryDTO.getFlagUrl());
    }
}
