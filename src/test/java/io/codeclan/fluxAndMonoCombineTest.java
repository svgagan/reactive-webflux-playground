package io.codeclan;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class fluxAndMonoCombineTest {

    @Test
    public void testFluxMerger(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C");
        Flux<String> stringFluxTwo = Flux.just("X", "Y", "Z");

        Flux<String> merStringFlux = Flux.merge(stringFluxOne, stringFluxTwo);

        StepVerifier.create(merStringFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void testFluxMergerWithDelay(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFluxTwo = Flux.just("X", "Y", "Z").delayElements(Duration.ofSeconds(1));

        Flux<String> merStringFlux = Flux.merge(stringFluxOne, stringFluxTwo); //Order sequence is not maintained

        StepVerifier.create(merStringFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void testFluxConcat(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C");
        Flux<String> stringFluxTwo = Flux.just("X", "Y", "Z");

        Flux<String> merStringFlux = Flux.concat(stringFluxOne, stringFluxTwo);

        StepVerifier.create(merStringFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void testFluxConcatWithDelay(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFluxTwo = Flux.just("X", "Y", "Z").delayElements(Duration.ofSeconds(1));

        Flux<String> merStringFlux = Flux.concat(stringFluxOne, stringFluxTwo); //Order sequence is maintained

        StepVerifier.create(merStringFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void testFluxZip(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C");
        Flux<String> stringFluxTwo = Flux.just("X", "Y", "Z");

        Flux<String> merStringFlux = Flux.zip(stringFluxOne, stringFluxTwo, (k1, k2) -> {return k1.concat(k2);});

        StepVerifier.create(merStringFlux.log())
                .expectSubscription()
                .expectNext("AX", "BY", "CZ")
                .verifyComplete();

    }
}
