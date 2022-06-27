package beerwebshopproject.beer.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class BeerNotFoundException extends AbstractThrowableProblem {
    public BeerNotFoundException(long id) {
        super(URI.create("beer/beer-not-found"),
                "Beer not found",
                Status.NOT_FOUND,
                String.format("Beer not found with id: %d", id));
    }
}
