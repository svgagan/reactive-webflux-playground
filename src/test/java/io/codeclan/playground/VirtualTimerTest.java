package io.codeclan.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimerTest {

    @Test
    public void withoutVirtualTimerTest(){

        Flux<String> stringFlux = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

//    Needs to verified more.
//    @Test
////    public void withVirtualTimerTest(){
////
////        VirtualTimeScheduler.getOrSet();
////
////        Flux<Long> longFlux = Flux.interval(Duration.ofSeconds(1)).take(3);
////
////        StepVerifier.withVirtualTime(() -> longFlux.log())
////                .expectSubscription()
////                .thenAwait(Duration.ofSeconds(3))
////                .expectNext(0l, 1l, 2l)
////                .verifyComplete();
////    }
}
