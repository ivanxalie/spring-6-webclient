package guru.springframework.client;

import com.fasterxml.jackson.databind.JsonNode;
import guru.springframework.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface BeerClient {
    Flux<String> beers();

    Flux<Map> beersAsMap();

    Flux<JsonNode> beersJsonNode();

    Flux<BeerDTO> beersDTO();

    Mono<BeerDTO> getBeerById(String id);

    Flux<BeerDTO> getBeerByStyle(String style);

    Mono<BeerDTO> createBeer(BeerDTO beer);

    Mono<BeerDTO> updateBeer(BeerDTO dto);

    Mono<BeerDTO> patchBeer(BeerDTO beerDTO);

    Mono<Void> deleteBeerById(String id);
}
