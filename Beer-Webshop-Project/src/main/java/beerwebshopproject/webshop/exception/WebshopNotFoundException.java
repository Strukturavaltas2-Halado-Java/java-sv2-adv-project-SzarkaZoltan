package beerwebshopproject.webshop.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class WebshopNotFoundException extends AbstractThrowableProblem {

    public WebshopNotFoundException(long id) {
        super(URI.create("webshop/webshop-not-found"),
                "Webshop not found",
                Status.NOT_FOUND,
                String.format("Webshop not found with id: %d", id));
    }
}
