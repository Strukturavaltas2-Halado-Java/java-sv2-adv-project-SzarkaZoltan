package beerwebshopproject.beer.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import javax.validation.constraints.NotBlank;
import java.net.URI;

public class IngredientIncludedException extends AbstractThrowableProblem {
    public IngredientIncludedException(long id, @NotBlank(message = "name can not be blank") String name) {
        super(URI.create("beer/ingredient-included"),
                "Ingredient already included",
                Status.NOT_FOUND,
                String.format("%s ingredient is already included in beer with id: %d",name, id));
    }
}
