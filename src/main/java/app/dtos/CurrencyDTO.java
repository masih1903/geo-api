package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class CurrencyDTO
{
    private String name;
    private String symbol;


    public CurrencyDTO(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
}
