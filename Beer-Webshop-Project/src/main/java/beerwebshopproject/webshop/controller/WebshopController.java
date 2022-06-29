package beerwebshopproject.webshop.controller;

import beerwebshopproject.webshop.dto.CreateWebshopCommand;
import beerwebshopproject.webshop.dto.WebshopDto;
import beerwebshopproject.service.BeerWebshopService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "get all webshops")
    public List<WebshopDto> listWebshops(@RequestParam Optional<String> prefix) {
        return service.getWebshops(prefix);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "create a webshop")
    public WebshopDto createWebshop(@RequestBody CreateWebshopCommand createWebshopCommand) {
        return service.createWebshop(createWebshopCommand);
    }

    @PutMapping("/{id}/beers")
    @Operation(summary = "assigns a beer to a webshop")
    public WebshopDto updateWebshopWithBeerById(@PathVariable("id") long webshopId, @RequestParam long beerId) {
        return service.updateWebshopWithBeerById(webshopId, beerId);
    }

    @PutMapping("/{id}/beers/{beerId}")
    @Operation(summary = "remove a beer from a webshop")
    public WebshopDto removeBeerFromWebshopById(@PathVariable("id") long webshopId, @PathVariable("beerId") long beerId) {
        return service.removeBeerFromWebshopById(webshopId, beerId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a webshop by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable("id") long id) {
        service.deleteWebshopById(id);
    }

}
