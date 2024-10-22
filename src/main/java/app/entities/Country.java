package app.entities;

import app.dtos.CountryDTO;
import app.dtos.CurrencyDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commonName;
    private String officialName;
    private String currencyName;
    private String currencySymbol;
    private String region;
    private long population;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> capitals;

    private String drivingSide;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> carSigns;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "language_code")
    @Column(name = "language_name")
    private Map<String, String> languages;

    public Country(CountryDTO countryDTO){

        this.commonName = countryDTO.getName().getCommon();
        this.officialName = countryDTO.getName().getOfficial();

        //Fra Karl
        // Access the first currency directly from the map
        CurrencyDTO currencyDTO = countryDTO.getCurrencies().values().stream().findFirst().orElse(null);
        this.currencyName = (currencyDTO != null) ? currencyDTO.getName() : null; // Or a default value
        this.currencySymbol = (currencyDTO != null) ? currencyDTO.getSymbol() : null; // Or a default value

        this.region = countryDTO.getRegion();
        this.population = countryDTO.getPopulation();
        this.capitals = countryDTO.getCapitals();
        this.drivingSide = countryDTO.getCar().getSide();
        this.carSigns = countryDTO.getCar().getSigns();
        this.languages = countryDTO.getLanguages();

    }

}
