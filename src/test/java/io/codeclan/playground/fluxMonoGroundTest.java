package io.codeclan.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class fluxMonoGroundTest {

    @Test
    public void fluxTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux")
                .log();
        demoFlux
                .subscribe(System.out::println, null ,() -> System.out.println("Reached OnComplete block!"));
    }

    @Test
    public void fluxErrorTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .log();
        demoFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Reached OnError block: "+e),
                        () -> System.out.println("Will Not Reach OnComplete block!"));
    }

    @Test
    public void fluxAfterErrorTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux")
                .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
                .concatWith(Flux.just("Reactive"))
                .log();
        demoFlux
                .subscribe(System.out::println,
                        (e) -> System.err.println("Reached OnError block: "+ e),
                        () -> System.out.println("Will Not Reach OnComplete block!"));
    }

    @Test
    public void flux_WithoutErrorsTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux").log();

        StepVerifier.create(demoFlux)
                .expectNext("Hello")
                .expectNext("World", "Welcome", "To", "Webflux")
                .expectComplete()
                .verify();

        StepVerifier.create(demoFlux)
                .expectNext("Hello")
                .expectNext("World", "Welcome", "To", "Webflux")
                .verifyComplete();

    }

    @Test
    public void flux_WithErrorsTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .log();

        StepVerifier.create(demoFlux)
                .expectNext("Hello")
                .expectNext("World", "Welcome", "To", "Webflux")
                .expectError(RuntimeException.class)
                .verify();

        StepVerifier.create(demoFlux)
                .expectNext("Hello")
                .expectNext("World", "Welcome", "To", "Webflux")
                .expectErrorMessage("Error Occurred")
                .verify();

    }

    @Test
    public void fluxSize_WithErrorsTest(){

        Flux<String> demoFlux = Flux.just("Hello", "World", "Welcome", "To", "Webflux")
                .concatWith(Flux.error(new RuntimeException("Error Occurred")))
                .log();

        StepVerifier.create(demoFlux)
                .expectNextCount(5)
                .expectError()
                .verify();

    }

    @Test
    public void monoTest(){

        Mono<String> demoFlux = Mono.just("Webflux")
                .log();

        StepVerifier.create(demoFlux)
                .expectNext("Webflux")
                .expectComplete()
                .verify();
    }

    @Test
    public void monoErrorTest(){

        Mono<Object> demoFlux = Mono.error(new RuntimeException("Error Occurred"))
                .log();

        StepVerifier.create(demoFlux)
                .expectError(RuntimeException.class)
                .verify();
    }
}
