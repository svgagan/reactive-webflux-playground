package io.codeclan;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class fluxAndMonoFactoryTest {

    List<String > countryList = Arrays.asList("India", "USA", "Russia");

    List<String > countryFilterList = Arrays.asList("India", "USA", "Russia", "Israel", "Italy", "Iran");

    @Test
    public void testFluxUsingIterables(){

        Flux<String> stringFlux = Flux.fromIterable(countryList).log();

        StepVerifier.create(stringFlux)
                .expectNext("India", "USA", "Russia")
                .verifyComplete();
    }

    @Test
    public void testFluxUsingStreams(){

        Flux<String> stringFlux = Flux.fromStream(countryList.stream()).log();

        StepVerifier.create(stringFlux)
                .expectNext("India", "USA", "Russia")
                .verifyComplete();
    }

    @Test
    public void testFluxUsingArrays(){

        Flux<String> stringFlux = Flux.fromArray(countryList.stream().toArray(String[]::new)).log();

        StepVerifier.create(stringFlux)
                .expectNext("India", "USA", "Russia")
                .verifyComplete();
    }

    @Test
    public void testMonoUsingJustOrEmpty(){

        Mono<String> stringFlux = Mono.justOrEmpty(null);

        StepVerifier.create(stringFlux.log())
                .verifyComplete();
    }

    @Test
    public void testMonoUsingSupplier(){

        Supplier<String> stringSupplier = () -> "webflux";

        Mono<String> stringFlux = Mono.fromSupplier(stringSupplier).log();

        StepVerifier.create(stringFlux)
                .expectNext("webflux")
                .verifyComplete();
    }

    @Test
    public void testFluxUsingRange(){

        Flux<Integer> integerFlux = Flux.range(1, 5).log();

        StepVerifier.create(integerFlux)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void testFluxFilters(){

        Flux<String> stringFlux = Flux.fromIterable(countryFilterList)
                .filter(country -> country.startsWith("I")).log();

        StepVerifier.create(stringFlux)
                .expectNext("India", "Israel", "Italy", "Iran")
                .verifyComplete();
    }

    @Test
    public void testFluxFiltersWithSize(){

        Flux<String> stringFlux = Flux.fromIterable(countryFilterList)
                .filter(country -> country.startsWith("I") && country.length() >= 5).log();

        StepVerifier.create(stringFlux)
                .expectNext("India", "Israel", "Italy")
                .verifyComplete();
    }
}
