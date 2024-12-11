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
public class CountryDTO
{
    private NameDTO name;
    private Map<String, CurrencyDTO> currencies = new HashMap<>(); // Initialize to avoid null
    private Map<String, String> languages;

    private List<String> capital;

    private String region;
    private long population;
    private CarDTO car;
    private FlagsDTO flags; // Flags DTO for at håndtere PNG-URL

    // Hvis du ikke har brug for SVG eller andre uventede felter, kan du markere klassen som ignorer ukendte felter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlagsDTO {
        public String png;
    }

    public String getFlagUrl()
    {
        return this.flags != null ? this.flags.png : null; // Returnér kun PNG-URL
    }

    public CountryDTO(Country country) {
        this.name = new NameDTO(country.getCommonName(), country.getOfficialName());
        this.currencies = Map.of("currency", new CurrencyDTO(country.getCurrencyName(), country.getCurrencySymbol()));
        this.languages = country.getLanguages();
        this.capital = country.getCapitals();
        this.region = country.getRegion();
        this.population = country.getPopulation();
        this.car = new CarDTO(country.getDrivingSide(), country.getCarSigns());

        // Opret FlagsDTO med PNG-URL
        this.flags = new FlagsDTO();
        this.flags.png = country.getFlag(); // Gemmer flag-URL fra entiteten
    }

    public static List<CountryDTO> toCountryDTOList(List<Country> countries)
    {
        return countries.stream().map(CountryDTO::new).collect(Collectors.toList());
    }

}
