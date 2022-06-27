package beerwebshopproject.beer.dto;

import beerwebshopproject.webshop.dto.WebshopDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BeerDto {
    private Long id;
    private String name;
    private String brand;
    private String type;
    private int price;
    private double alcohol;
    private List<IngredientDto> ingredients = new ArrayList<>();
    private List<WebshopDto> webshops = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeerDto beerDto = (BeerDto) o;
        return price == beerDto.price && Double.compare(beerDto.alcohol, alcohol) == 0 && Objects.equals(name, beerDto.name) && Objects.equals(brand, beerDto.brand) && Objects.equals(type, beerDto.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand, type, price, alcohol);
    }
}
