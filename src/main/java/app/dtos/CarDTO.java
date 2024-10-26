package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class CarDTO {
    private List<String> signs;
    private String side;

    public CarDTO(String side, List<String> signs) {
        this.side = side;
        this.signs = signs;
    }
}
