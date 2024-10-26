package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor

public class CurrencyDTO {
    private String name;
    private String symbol;


    public CurrencyDTO(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
