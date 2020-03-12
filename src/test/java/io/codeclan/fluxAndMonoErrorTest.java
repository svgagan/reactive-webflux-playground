package io.codeclan;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class fluxAndMonoErrorTest {

    @Test
    public void testFluxError(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testFluxErrorHandlingUsingOnErrorResume(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorResume(e -> {
                    System.out.println(e);
                    return Flux.just("default", "error");
                })
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default", "error")
                .verifyComplete();
    }

    @Test
    public void testFluxErrorHandlingUsingOnErrorReturn(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default")
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void testFluxErrorHandlingUsingOnErrorMap(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void testFluxErrorHandlingUsingOnErrorMapWithRetry(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retry(2)
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void testFluxErrorHandlingUsingOnErrorMapWithRetryBackoff(){

        Flux<String> stringFluxOne = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .concatWith(Flux.just("D"))
                .onErrorMap((e) -> new CustomException(e))
                .retryBackoff(2, Duration.ofSeconds(5))
                .log();

        StepVerifier.create(stringFluxOne)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}
