package app.entities;

import app.dtos.CountryDTO;
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

    @ElementCollection
    private List<String> capitals;

    private String drivingSide;

    @ElementCollection
    private List<String> carSigns;

    @ElementCollection
    @MapKeyColumn(name = "language_code")
    @Column(name = "language_name")
    private Map<String, String> languages;

    public Country(CountryDTO countryDTO){

        this.commonName = countryDTO.getName().getCommon();
        this.officialName = countryDTO.getName().getOfficial();
        this.currencyName = countryDTO.getCurrencies().get("currencyName").getName();
        this.currencySymbol = countryDTO.getCurrencies().get("currencySymbol").getSymbol();
        this.region = countryDTO.getRegion();
        this.population = countryDTO.getPopulation();
        this.capitals = countryDTO.getCapitals();
        this.drivingSide = countryDTO.getCar().getSide();
        this.carSigns = countryDTO.getCar().getSigns();
        this.languages = countryDTO.getLanguages();

    }

}
