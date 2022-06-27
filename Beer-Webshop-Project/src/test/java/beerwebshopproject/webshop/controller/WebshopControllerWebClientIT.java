package beerwebshopproject.webshop.controller;

import beerwebshopproject.beer.dto.BeerDto;
import beerwebshopproject.beer.dto.CreateBeerCommand;
import beerwebshopproject.beer.dto.CreateIngredientCommand;
import beerwebshopproject.webshop.dto.CreateWebshopCommand;
import beerwebshopproject.webshop.dto.WebshopDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(statements = {"delete from beers_webshops", "delete from beer_ingredients", "delete from beers", "delete from webshops"})
class WebshopControllerWebClientIT {

    @Autowired
    WebTestClient webTestClient;

    BeerDto beerDto;
    WebshopDto webshopDto;

    @BeforeEach
    void init() {
        beerDto = webTestClient.post()
                .uri("api/beers")
                .bodyValue(new CreateBeerCommand(
                        "Beer Sans Wheat",
                        "Beer Sans Brewery",
                        "Wheat",
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
                                        ))
                        )))
                .exchange()
                .expectBody(WebshopDto.class)
                .returnResult().getResponseBody();
    }

    @Test
    void testCreateWebshop() {
        WebshopDto actual = webTestClient.post()
                .uri("api/webshops")
                .bodyValue(new CreateWebshopCommand("Awesome Beers", "jane.doe@gmail.com",
                        List.of(
                                new CreateBeerCommand(
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
                                        ))
                        )))
                .exchange()
                .expectBody(WebshopDto.class)
                .returnResult().getResponseBody();
        assertThat(actual.getEmailAddress()).isEqualTo("jane.doe@gmail.com");
    }

    @Test
    void testGetAllWebshops() {
        webTestClient.post()
                .uri("api/webshops")
                .bodyValue(new CreateWebshopCommand("Long Beers", "jane.doe@gmail.com",
                        Arrays.asList(
                                new CreateBeerCommand(
                                        "Beer Sans Corn",
                                        "Beer Sans Brewery",
                                        "Corn",
                                        910,
                                        0.129,
                                        List.of())
                        )))
                .exchange();
        webTestClient.get()
                .uri("api/webshops")
                .exchange()
                .expectBodyList(WebshopDto.class)
                .hasSize(2)
                .contains(new WebshopDto("Cool Beers", "john.doe@gmail.com"));
    }

    @Test
    void testDeleteWebshopByIdButNoFound() {
        Problem p = webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("api/webshops/{id}").build(10))
                .exchange()
                .expectBody(Problem.class)
                .returnResult().getResponseBody();
        assertEquals("Webshop not found with id: 10", p.getDetail());
    }

    @Test
    void updateWebshopWithBeerById() {
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/webshops/{id}/beers").queryParam("beerId", beerDto.getId()).build(webshopDto.getId()))
                .exchange();

        BeerDto actual = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(beerDto.getId()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        assertEquals(1, actual.getWebshops().size());
        assertThat(actual).extracting(BeerDto::getWebshops).asList().containsExactly(webshopDto);
    }

    @Test
    void testRemoveBeerFromWebshopById() {
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("api/webshops/{id}/beers").queryParam("beerId", beerDto.getId()).build(webshopDto.getId()))
                .exchange();
        webTestClient.put()
                .uri(uriBuilder -> uriBuilder.path("/api/webshops/{id}/beers/{beerId}").build(webshopDto.getId(), beerDto.getId()))
                .exchange();

        BeerDto actualBeerDto = webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("api/beers/{id}").build(beerDto.getId()))
                .exchange()
                .expectBody(BeerDto.class)
                .returnResult().getResponseBody();
        assertEquals(0, actualBeerDto.getWebshops().size());
        assertThat(actualBeerDto).extracting(BeerDto::getWebshops).asList().doesNotContain(webshopDto);
    }

    @Test
    void testDeleteWebshopById() {
        webTestClient.delete()
                .uri(uriBuilder -> uriBuilder.path("/api/webshops/{id}").build(webshopDto.getId()))
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}