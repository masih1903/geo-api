package app.dtos;

import app.entities.Country;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CountryDTO
{
    private NameDTO name;
    private Map<String, CurrencyDTO> currencies = new HashMap<>(); // Initialize to avoid null
    private Map<String, String> languages;

    @JsonProperty("capital")
    private List<String> capitals;

    private String region;
    private long population;
    private CarDTO car;

    public CountryDTO(Country country) {
        this.name = new NameDTO(country.getCommonName(), country.getOfficialName());
        this.currencies = Map.of("currency", new CurrencyDTO(country.getCurrencyName(), country.getCurrencySymbol()));
        this.languages = country.getLanguages();
        this.capitals = country.getCapitals();
        this.region = country.getRegion();
        this.population = country.getPopulation();
        this.car = new CarDTO(country.getDrivingSide(), country.getCarSigns());

    }
}
