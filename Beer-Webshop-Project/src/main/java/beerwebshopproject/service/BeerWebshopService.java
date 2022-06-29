package beerwebshopproject.service;

import beerwebshopproject.beer.dto.CreateIngredientCommand;
import beerwebshopproject.beer.dto.IngredientDto;
import beerwebshopproject.beer.exception.BeerNotFoundException;
import beerwebshopproject.beer.exception.IngredientIncludedException;
import beerwebshopproject.beer.model.Beer;
import beerwebshopproject.beer.dto.BeerDto;
import beerwebshopproject.beer.dto.CreateBeerCommand;
import beerwebshopproject.beer.model.Ingredient;
import beerwebshopproject.beer.repository.BeerRepository;
import beerwebshopproject.webshop.dto.CreateWebshopCommand;
import beerwebshopproject.webshop.dto.WebshopDto;
import beerwebshopproject.webshop.exception.WebshopNotFoundException;
import beerwebshopproject.webshop.model.Webshop;
import beerwebshopproject.webshop.repository.WebshopRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BeerWebshopService {

    private BeerRepository beerRepository;
    private WebshopRepository webshopRepository;
    private ModelMapper modelMapper;

    public List<BeerDto> getAllBeers(Optional<String> brand, Optional<String> type) {
        List<Beer> beers = beerRepository.findBeersByBrandAndType(brand, type);
        return beers.stream()
                .map(beer -> modelMapper.map(beer, BeerDto.class))
                .collect(Collectors.toList());
    }

    public BeerDto createNewBeer(CreateBeerCommand createBeerCommand) {
        List<Ingredient> ingredients = new ArrayList<>();
        if (!createBeerCommand.getIngredients().isEmpty()) {
            ingredients = createBeerCommand.getIngredients().stream()
                    .map(i -> modelMapper.map(i, Ingredient.class))
                    .collect(Collectors.toList());
        }
        Beer beer = new Beer(
                createBeerCommand.getName(),
                createBeerCommand.getBrand(),
                createBeerCommand.getType(),
                createBeerCommand.getPrice(),
                createBeerCommand.getAlcohol()
                , ingredients
        );
        log.info("Beer has been created");
        log.debug("Beer has been created with name {}", createBeerCommand.getName());
        beerRepository.save(beer);
        return modelMapper.map(beer, BeerDto.class);
    }

    public void deleteBeerById(long id) {
        try {
            beerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException erdae) {
            throw new BeerNotFoundException(id);
        }
    }

    private Beer findBeerById(long id) {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    public BeerDto getBeerById(long id) {
        return modelMapper.map(findBeerById(id),
                BeerDto.class);
    }

    public BeerDto updateBeerByIdWithWebshop(long beerId, long webshopId) {
        Beer beer = findBeerById(beerId);
        Webshop webshop = findWebshopById(webshopId);
        beer.addWebshop(webshop);
        webshop.addBeer(beer);
        return modelMapper.map(beer, BeerDto.class);
    }

    public Set<String> getAllBrands() {
        return beerRepository.getAllBrands();
    }

    public List<WebshopDto> getWebshops(Optional<String> prefix) {
        List<Webshop> webshops = webshopRepository.findAllWithBeers();
        return webshops.stream()
                .map(shop -> modelMapper.map(shop, WebshopDto.class))
                .collect(Collectors.toList());
    }

    public WebshopDto createWebshop(CreateWebshopCommand command) {
        List<Beer> beers = command.getBeers().stream().map(b -> modelMapper.map(b, Beer.class)).collect(Collectors.toList());
        Webshop webshop = new Webshop(
                command.getName(),
                command.getEmailAddress(),
                beers);
        beers.forEach(b -> b.addWebshop(webshop));
        webshopRepository.save(webshop);
        return modelMapper.map(webshop, WebshopDto.class);
    }

    public void deleteWebshopById(long id) {
        deleteBeerAndWebshopConnections(id);
        webshopRepository.deleteById(id);
    }

    private void deleteBeerAndWebshopConnections(long id) {
        Webshop webshop = findWebshopById(id);
        webshop.getBeers().forEach(b -> b.removeWebshop(webshop));
        webshop.removeAllBeers();
    }

    public BeerDto addOneIngredientsById(long id, CreateIngredientCommand command) {
        Beer beer = findBeerById(id);
        Ingredient ingredient = new Ingredient(command.getName(), command.getRatio());
        if (beer.getIngredients().contains(ingredient)) {
            throw new IngredientIncludedException(id, command.getName());
        }
        beer.addIngredients(ingredient);
        return modelMapper.map(beer, BeerDto.class);
    }

    public WebshopDto updateWebshopWithBeerById(long webshopId, long beerId) {
        Webshop webshop = findWebshopById(webshopId);
        Beer beer = findBeerById(beerId);
        webshop.addBeer(beer);
        beer.addWebshop(webshop);
        return modelMapper.map(webshop, WebshopDto.class);
    }

    private Webshop findWebshopById(long id) {
        return webshopRepository.findById(id)
                .orElseThrow(() -> new WebshopNotFoundException(id));
    }

    public List<IngredientDto> getIngredientsByBeerId(long id) {
        Beer beer = findBeerById(id);
        return beer.getIngredients().stream()
                .map(b -> modelMapper.map(b, IngredientDto.class))
                .collect(Collectors.toList());
    }

    public WebshopDto removeBeerFromWebshopById(long webshopId, long beerId) {
        Webshop webshop = findWebshopById(webshopId);
        Beer beer = findBeerById(beerId);
        if (!webshop.getBeers().contains(beer)) {
            throw new BeerNotFoundException(beerId);
        }
        webshop.removeBeer(beer);
        beer.removeWebshop(webshop);
        return modelMapper.map(webshop, WebshopDto.class);
    }
}
