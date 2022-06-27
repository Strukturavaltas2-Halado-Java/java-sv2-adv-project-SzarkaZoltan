package beerwebshopproject.webshop.controller;

import beerwebshopproject.webshop.dto.CreateWebshopCommand;
import beerwebshopproject.webshop.dto.WebshopDto;
import beerwebshopproject.service.BeerWebshopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/webshops")
@Tag(name = "Operations on webshops")
@AllArgsConstructor
public class WebshopController {

    private BeerWebshopService service;

    @GetMapping
    public List<WebshopDto> listWebshops(@RequestParam Optional<String> prefix) {
        return service.getWebshops(prefix);
    }

    @PostMapping
    public WebshopDto createWebshop(@RequestBody CreateWebshopCommand createWebshopCommand) {
        return service.createWebshop(createWebshopCommand);
    }

    @PutMapping("/{id}/beers")
    public WebshopDto updateWebshopWithBeerById(@PathVariable("id") long webshopId, @RequestParam long beerId) {
        return service.updateWebshopWithBeerById(webshopId, beerId);
    }

    @PutMapping("/{id}/beers/{beerId}")
    public WebshopDto removeBeerFromWebshopById(@PathVariable("id") long webshopId, @PathVariable("beerId") long beerId) {
        return service.removeBeerFromWebshopById(webshopId, beerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable("id") long id) {
        service.deleteWebshopById(id);
    }

}
