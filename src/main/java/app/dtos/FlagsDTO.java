package app.dtos;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlagsDTO {
    private String png;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlagsDTO flagsDTO = (FlagsDTO) o;
        return Objects.equals(png, flagsDTO.png);
    }

    @Override
    public int hashCode() {
        return Objects.hash(png);
    }
}
