package beerwebshopproject.beer.controller;

import beerwebshopproject.beer.dto.BeerDto;
import beerwebshopproject.beer.dto.CreateBeerCommand;
import beerwebshopproject.beer.dto.CreateIngredientCommand;
import beerwebshopproject.beer.dto.IngredientDto;
import beerwebshopproject.service.BeerWebshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/beers")
@Tag(name = "Operations on beers")
@AllArgsConstructor
public class BeerController {

    private BeerWebshopService beerService;

    @GetMapping
    @Operation(summary = "get all beers")
    public List<BeerDto> getAllBeers(@RequestParam Optional<String> brand, @RequestParam Optional<String> type) {
        return beerService.getAllBeers(brand,type);
    }

    @GetMapping("/{id}")
    @Operation(summary = "get beer by id")
    public BeerDto getBeerById(@PathVariable("id") long id) {
        return beerService.getBeerById(id);
    }

    @GetMapping("/brands")
    @Operation(summary = "get all beer brands")
    public Set<String> getBeerBrands(){
        return beerService.getAllBrands();
    }

    @GetMapping("/{id}/ingredients")
    @Operation(summary = "get all ingredients of a beer by id")
    public List<IngredientDto> getIngredientsByBeerId(@PathVariable("id") long id){
        return beerService.getIngredientsByBeerId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "creates a beer")
    @ApiResponse(responseCode = "201", description = "beer has been created")
    public BeerDto createNewBeer(@Valid @RequestBody CreateBeerCommand createBeerCommand) {
        return beerService.createNewBeer(createBeerCommand);
    }

    @PostMapping("/{id}/ingredients")
    @Operation(summary = "creates an ingredient and assigns to a beer by id")
    public BeerDto addOneIngredientById(@PathVariable("id") long id, @Valid @RequestBody CreateIngredientCommand ingredientCommands) {
        return beerService.addOneIngredientsById(id, ingredientCommands);
    }

    @PutMapping("/{id}/webshops")
    @Operation(summary = "assings a webshop to a beer by ids")
    public BeerDto updateBeerByIdWithWebshop(@PathVariable("id") long beerId,@RequestParam long webshopId) {
        return beerService.updateBeerByIdWithWebshop(beerId, webshopId);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete beer by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable("id") long id) {
        beerService.deleteBeerById(id);
    }
}
