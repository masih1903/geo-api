package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class CountryDTO
{
    private NameDTO name;
    private Map<String, CurrencyDTO> currencies;
    @JsonProperty("languages")
    private Map<String, String> language;
    private List<String> capital;
    private String region;
    private long population;
    private CarDTO car;
}
