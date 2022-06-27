package beerwebshopproject.beer.controller;

import beerwebshopproject.beer.dto.BeerDto;
import beerwebshopproject.beer.dto.CreateBeerCommand;
import beerwebshopproject.beer.dto.CreateIngredientCommand;
import beerwebshopproject.beer.dto.IngredientDto;
import beerwebshopproject.webshop.dto.CreateWebshopCommand;
import beerwebshopproject.webshop.dto.WebshopDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from beers_webshops", "delete from beer_ingredients", "delete from beers", "delete from webshops"})
class BeerControllerWebClientIT {

    @Autowired
    WebTestClient webTestClient;

    BeerDto beerDto;
    WebshopDto webshopDto;

    @BeforeEach
    void init() {
        beerDto = webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Corn",
                        "Beer Sans Brewery",
                        "Corn",
                        910,
                        0.129,
                        Arrays.asList(
                                new CreateIngredientCommand("salt", 0.004),
                                new CreateIngredientCommand("sugar", 0.027),
                                new CreateIngredientCommand("barley", 0),
                                new CreateIngredientCommand("wheat", 0),
                                new CreateIngredientCommand("corn", 0.221)
                        )))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();

        webshopDto = webTestClient.post()
                .uri("api/webshops")
                .bodyValue(new CreateWebshopCommand("Cool Beers", "john.doe@gmail.com",
                        List.of(
                                new CreateBeerCommand(
                                        "Beer Sans Barley",
                                        "Beer Sans Brewery",
                                        "Corn",
                                        1200,
                                        0.145,
                                        Arrays.asList(
                                                new CreateIngredientCommand("salt", 0.004),
                                                new CreateIngredientCommand("sugar", 0.027),
                                                new CreateIngredientCommand("barley", 0.25),
                                                new CreateIngredientCommand("wheat", 0),
                                                new CreateIngredientCommand("corn", 0.021)
                                        ))
                        )))
                .exchange()
                .expectBody(WebshopDto.class)
                .returnResult()
                .getResponseBody();
    }



    @Test
    void testGetAllBeers() {
        webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans White",
                        "Wheat",
                        1500,
                        0.2,
                        List.of()))
                .exchange()
                .expectBody(BeerDto.class);

        List<BeerDto> beers = webTestClient.get()
                .uri("api/beers")
                .exchange()
                .expectBodyList(BeerDto.class)
                .returnResult().getResponseBody();
        assertThat(beers).hasSize(3).extracting(BeerDto::getName)
                .containsExactly("Beer Sans Corn", "Beer Sans Barley", "Beer Sans Wheat");
    }

    @Test
    void testGetBeerById() {
        BeerDto actual = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(beerDto.getId()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        assertEquals(910, actual.getPrice());
    }

    @Test
    void testGetBeerBrands() {
        webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans White",
                        "Wheat",
                        1500,
                        0.2,
                        List.of()))
                .exchange()
                .expectBody(BeerDto.class);

        webTestClient.get()
                .uri("api/beers/brands")
                .exchange()
                .expectBody(TreeSet.class)
                .value(b -> assertThat(b).containsExactly("Beer Sans Brewery", "Beer Sans White"));
    }

    @Test
    void testgetIngredientsByBeerId(){
        List<IngredientDto> ingredientDtos = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}/ingredients").build(beerDto.getId()))
                .exchange()
                .expectBodyList(IngredientDto.class)
                .returnResult().getResponseBody();

        assertThat(ingredientDtos).contains(new IngredientDto("salt", 0.004),
                new IngredientDto("sugar", 0.027),
                new IngredientDto("barley", 0),
                new IngredientDto("wheat", 0),
                new IngredientDto("corn", 0.221));
    }

    @Test
    void testCreateBeer() {
        BeerDto actual = webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans Brewery",
                        "Wheat",
                        1500,
                        0.2,
                        Arrays.asList(
                                new CreateIngredientCommand("salt", 0.004),
                                new CreateIngredientCommand("sugar", 0.027),
                                new CreateIngredientCommand("barley", 0),
                                new CreateIngredientCommand("wheat", 0.215),
                                new CreateIngredientCommand("corn", 0.021)
                        )))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        assertThat(actual.getBrand()).isEqualTo("Beer Sans Brewery");
    }

    @Test
    void testGetBeerByIdButNotFound() {
        Problem p = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(10))
                .exchange()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();
        assertEquals("Beer not found with id: 10", p.getDetail());
    }

    @Test
    void testAddOneIngredientById(){
        BeerDto newBeerDto = webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans White",
                        "Wheat",
                        1500,
                        0.2,
                        List.of()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        BeerDto updatedBeerDto = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}/ingredients")
                        .build(newBeerDto.getId()))
                .bodyValue(new CreateIngredientCommand("wheat", 0.15))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();

        assertEquals("wheat",updatedBeerDto.getIngredients().get(0).getName());
        assertEquals(0.15,updatedBeerDto.getIngredients().get(0).getRatio());
    }

    @Test
    void testAddWrongIngredientsById() {
        BeerDto actual = webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans White",
                        "Wheat",
                        1500,
                        0.2,
                        List.of()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();

        ConstraintViolationProblem cvp = webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}/ingredients")
                        .build(actual.getId()))
                .bodyValue(new CreateIngredientCommand("corn", 0.5))
                .exchange()
                .expectBody(ConstraintViolationProblem.class)
                .returnResult().getResponseBody();

        assertEquals("ratio can not be more than 0.3", cvp.getViolations().get(0).getMessage());
    }

    @Test
    void testDeleteBeerById() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(beerDto.getId()))
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void testUpdateBeerByIdWithWebshop() {
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}/webshops").queryParam("webshopId", webshopDto.getId()).build(beerDto.getId()))
                .exchange();

        BeerDto actual = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(beerDto.getId()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        assertEquals(1,actual.getWebshops().size());
        assertEquals("Cool Beers", actual.getWebshops().get(0).getName());
    }
}