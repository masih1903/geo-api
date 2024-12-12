package app.entities;

import app.dtos.CountryDTO;
import app.dtos.CurrencyDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String cca2;
    private Boolean independent;
    private String commonName;
    private String officialName;
    private String currencyName;
    private String currencySymbol;
    private String region;
    private long population;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> capitals = new ArrayList<>(); // Ensure initialization to avoid nulls

    private String drivingSide;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> carSigns;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "language_code")
    @Column(name = "language_name")
    private Map<String, String> languages;
    private String flag; // Gem PNG-URL

    public Country(String cca2, Boolean independent, String commonName, String officialName, String currencyName, String currencySymbol,
                   String region, long population, List<String> capitals, String drivingSide,
                   List<String> carSigns, Map<String, String> languages, String flag) {

        this.cca2 = cca2;
        this.independent = independent;
        this.commonName = commonName;
        this.officialName = officialName;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
        this.region = region;
        this.population = population;
        this.capitals = capitals;
        this.drivingSide = drivingSide;
        this.carSigns = carSigns;
        this.languages = languages;
        this.flag = flag;
    }

    public Country(CountryDTO countryDTO) {
        this.cca2 = countryDTO.getCca2();
        this.independent = countryDTO.getIndependent();
        this.commonName = countryDTO.getName().getCommon();
        this.officialName = countryDTO.getName().getOfficial();
        this.currencyName = countryDTO.getCurrencies().values().stream()
                .findFirst()
                .map(CurrencyDTO::getName)
                .orElse(null);
        this.currencySymbol = countryDTO.getCurrencies().values().stream()
                .findFirst()
                .map(CurrencyDTO::getSymbol)
                .orElse(null);
        this.region = countryDTO.getRegion();
        this.population = countryDTO.getPopulation();
        this.capitals = countryDTO.getCapital();
        this.drivingSide = countryDTO.getCar().getSide();
        this.carSigns = countryDTO.getCar().getSigns();
        this.languages = countryDTO.getLanguages();
        this.flag = countryDTO.getFlagUrl(); // Gem kun PNG URL
    }

}
