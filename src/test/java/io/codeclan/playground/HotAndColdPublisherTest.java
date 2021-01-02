package io.codeclan.playground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class HotAndColdPublisherTest {

    @Test
    public void ColdPublisherTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(value -> {System.out.println("Subscriber 1:"+value);}); //Emits data from beginning.
        Thread.sleep(3000);

        stringFlux.subscribe(value -> {System.out.println("Subscriber 2:"+value);}); //Emits data from beginning.
        Thread.sleep(4000);
    }

    @Test
    public void HotPublisherTest() throws InterruptedException {

        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F").delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> stringConnectableFlux = stringFlux.publish();
        stringConnectableFlux.connect();

        stringConnectableFlux.subscribe(value -> {System.out.println("Subscriber 1:"+value);}); //Emits data from beginning.
        Thread.sleep(3000);

        stringConnectableFlux.subscribe(value -> {System.out.println("Subscriber 2:"+value);}); //Emits data from time when its subscribed.
        Thread.sleep(4000);
    }
}
