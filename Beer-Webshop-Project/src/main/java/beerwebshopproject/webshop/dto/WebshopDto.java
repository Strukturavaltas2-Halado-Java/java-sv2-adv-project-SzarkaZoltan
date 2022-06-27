package beerwebshopproject.webshop.dto;

import beerwebshopproject.beer.dto.BeerDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WebshopDto {
    private long id;
    private String name;
    private String emailAddress;
    @JsonIgnore
    private List<BeerDto> beers = new ArrayList<>();

    public WebshopDto(String name, String emailAddress) {
        this.name = name;
        this.emailAddress = emailAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WebshopDto that = (WebshopDto) o;
        return Objects.equals(name, that.name) && Objects.equals(emailAddress, that.emailAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, emailAddress);
    }
}
