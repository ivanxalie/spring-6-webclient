package guru.springframework.client;

import guru.springframework.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Timeout(5)
class BeerClientImplTest {
    @Autowired
    BeerClient client;

    public static void execute(Consumer<AtomicBoolean> subscriptionLogic) {
        AtomicBoolean subscriptionCompleted = new AtomicBoolean();
        subscriptionLogic.accept(subscriptionCompleted);
        await().untilTrue(subscriptionCompleted);
    }

    @Test
    void testBeers() {
        execute(subscriptionCompleted ->
                client.beers().subscribe(response -> {
                    subscriptionCompleted.set(true);
                    System.out.println(response);
                }));
    }

    @Test
    void testBeersMap() {
        execute(subscriptionCompleted ->
                client.beersAsMap().subscribe(response -> {
                    subscriptionCompleted.set(true);
                    System.out.println(response);
                }));
    }

    @Test
    void testGetBeerJson() {
        execute(subscriptionCompleted ->
                client.beersJsonNode().subscribe(jsonNode -> {
                    System.out.println(jsonNode.toPrettyString());
                    subscriptionCompleted.set(true);
                }));
    }

    @Test
    void testGetBeerDTO() {
        execute(subscriptionCompleted ->
                client.beersDTO().subscribe(beerDTO -> {
                    System.out.println(beerDTO);
                    subscriptionCompleted.set(true);
                }));
    }

    @Test
    void testGetBeerById() {
        execute(subscriptionCompleted ->
                client.beersDTO()
                        .flatMap(beerDTO -> client.getBeerById(beerDTO.getId()))
                        .subscribe(beerDTO -> {
                            System.out.println(beerDTO.getName());
                            subscriptionCompleted.set(true);
                        }));
    }

    @Test
    void testGetBeerByStyle() {
        execute(subscriptionCompleted ->
                client.getBeerByStyle("PALE_ALE")
                        .subscribe(dto -> {
                            System.out.println(dto);
                            subscriptionCompleted.set(true);
                        }));
    }

    @Test
    void testCreateBeer() {
        execute(subscriptionCompleted -> {
            BeerDTO beer = createBeer();

            client.createBeer(beer)
                    .subscribe(dto -> {
                        System.out.println(dto);
                        subscriptionCompleted.set(true);
                    });
        });
    }

    BeerDTO createBeer() {
        return BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .name("Mongo Bobs")
                .style("IPA")
                .quantityOnHand(500)
                .upc("12345")
                .build();
    }

    @Test
    void testUpdate() {
        String newName = "New Name";
        execute(subscriptionCompleted ->
                client
                        .beersDTO()
                        .next()
                        .doOnNext(beer -> beer.setName(newName))
                        .flatMap(dto -> client.updateBeer(dto))
                        .subscribe(dto -> {
                            System.out.println(dto);
                            subscriptionCompleted.set(true);
                        }));
    }

    @Test
    void testPatch() {
        String newName = "New Name";

        execute(subscriptionCompleted -> {
            client
                    .beersDTO()
                    .next()
                    .doOnNext(beerDTO -> beerDTO.setName(newName))
                    .flatMap(beerDTO -> client.patchBeer(beerDTO))
                    .subscribe(dto -> {
                        System.out.println(dto);
                        subscriptionCompleted.set(true);
                    });
        });
    }

    @Test
    void testOnDelete() {
        execute(subscriptionCompleted ->
                client
                        .beersDTO()
                        .next()
                        .flatMap(beerDTO -> client.deleteBeerById(beerDTO.getId()))
                        .doOnSuccess(unused -> subscriptionCompleted.set(true))
                        .subscribe());
    }
}