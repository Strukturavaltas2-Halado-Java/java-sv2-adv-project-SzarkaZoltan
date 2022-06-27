package beerwebshopproject.beer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBeerCommand {

    @Schema(description = "name of the beer", example = "Beer Sans Corn")
    @NotBlank(message = "name can not be blank")
    private String name;
    @Schema(description = "brand of the beer", example = "Beer Sans Brewery")
    private String brand;
    @Schema(description = "type of the beer", example = "Corn")
    private String type;
    @Min(value = 500, message = "price can not be less than 500")
    @Max(value = 10_000, message = "price can not be more than 10_000")
    @Schema(description = "price of the beer", example = "910")
    private int price;
    @DecimalMax(value = "0.8", message = "alcohol can not be more than 0.8")
    @PositiveOrZero(message = "alcohol ratio can not be negative")
    @Schema(description = "alcohol of the beer", example = "0.129")
    private double alcohol;
    @Schema(description = "ingredients of the beer", example = "[]")
    private List<CreateIngredientCommand> ingredients = new ArrayList<>();
}
