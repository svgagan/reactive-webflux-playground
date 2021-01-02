package io.codeclan.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class fluxAndMonoTransformTest {

    List<String > countryList = Arrays.asList("India", "USA", "Russia", "Israel", "Italy", "Iran");

    @Test
    public void fluxTransformUsingMaps(){

        Flux<String> stringFlux = Flux.fromIterable(countryList)
                .map(country -> country.toUpperCase()).log();

        StepVerifier.create(stringFlux)
                .expectNext("INDIA", "USA", "RUSSIA", "ISRAEL", "ITALY", "IRAN")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingMaps_Length(){

        Flux<Integer> stringFlux = Flux.fromIterable(countryList)
                .map(country -> country.length()).log();

        StepVerifier.create(stringFlux)
                .expectNext(5, 3, 6, 6, 5, 4)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingMapsAndFilters(){

        Flux<String> stringFlux = Flux.fromIterable(countryList)
                .filter(country -> country.length() >= 5 )
                .map(country -> country.toUpperCase()).log();

        StepVerifier.create(stringFlux)
                .expectNext("INDIA", "RUSSIA", "ISRAEL", "ITALY")
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingFlatMaps(){

        Flux<String> stringFlux = Flux.fromIterable(countryList)
                .flatMap(country -> {
                    return Flux.fromIterable(tranformTheData(country));
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> tranformTheData(String country) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.asList(country, "Guys");

    }

    @Test
    public void fluxTransformUsingFlatMaps_parallel(){

        Flux<String> stringFlux = Flux.fromIterable(countryList)
                .window(2)
                .flatMap(country ->
                    country.map(this::tranformTheData).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void fluxTransformUsingFlatMaps_parallel_maintainOrder(){

        Flux<String> stringFlux = Flux.fromIterable(countryList)
                .window(2)
                .flatMapSequential(country ->
                        country.map(this::tranformTheData).subscribeOn(Schedulers.parallel()))
                .flatMap(s -> Flux.fromIterable(s))
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }
}
