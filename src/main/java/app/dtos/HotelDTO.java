package app.dtos;

import app.entities.Hotel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class HotelDTO {

    private Integer id;
    private String name;
    private String address;

    public HotelDTO(Hotel hotel) {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
    }

    public static List<HotelDTO> toHotelDtoList(List<Hotel> hotels) {
        return hotels.stream().map(HotelDTO::new).toList();
    }
}
