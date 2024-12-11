package app.dtos;

import app.entities.Country;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CountryDTO {
    private NameDTO name;
    private Map<String, CurrencyDTO> currencies = new HashMap<>(); // Initialize to avoid null
    private Map<String, String> languages;

    private List<String> capital;

    private String region;
    private long population;
    private CarDTO car;
    private String flag;

    public CountryDTO(Country country) {
        this.name = new NameDTO(country.getCommonName(), country.getOfficialName());
        this.currencies = Map.of("currency", new CurrencyDTO(country.getCurrencyName(), country.getCurrencySymbol()));
        this.languages = country.getLanguages();
        this.capital = country.getCapitals();
        this.region = country.getRegion();
        this.population = country.getPopulation();
        this.car = new CarDTO(country.getDrivingSide(), country.getCarSigns());
        this.flag = country.getFlag();
    }

    public static List<CountryDTO> toCountryDTOList(List<Country> countries) {
        return countries.stream().map(CountryDTO::new).collect(Collectors.toList());
    }

}
