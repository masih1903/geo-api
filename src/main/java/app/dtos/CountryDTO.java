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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDTO {
    private String cca2;
    private Boolean independent;
    private NameDTO name;
    private Map<String, CurrencyDTO> currencies = new HashMap<>();
    private Map<String, String> languages;
    private List<String> capital;
    private String region;
    private long population;
    private CarDTO car;
    private FlagsDTO flags; // Brug den nye FlagsDTO-klasse

    public String getFlagUrl() {
        return this.flags != null ? this.flags.getPng() : null;
    }

    public CountryDTO(Country country) {
        this.cca2 = country.getCca2();
        this.independent = country.getIndependent();
        this.name = new NameDTO(country.getCommonName(), country.getOfficialName());
        this.currencies = Map.of("currency", new CurrencyDTO(country.getCurrencyName(), country.getCurrencySymbol()));
        this.languages = country.getLanguages();
        this.capital = country.getCapitals();
        this.region = country.getRegion();
        this.population = country.getPopulation();
        this.car = new CarDTO(country.getDrivingSide(), country.getCarSigns());

        // Opret FlagsDTO med PNG-URL
        this.flags = new FlagsDTO();
        this.flags.setPng(country.getFlag()); // Gem PNG-URL fra entiteten
    }

    public static List<CountryDTO> toCountryDTOList(List<Country> countries) {
        return countries.stream().map(CountryDTO::new).collect(Collectors.toList());
    }
}
