package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
public class Sum42CatorController {
    @GetMapping("/dub42cat/{x}")
    int dub42cat(@PathVariable("x") int x) {
        WebClient client = WebClient.create("http://localhost:8081");
        String response = client.get().uri("/dub/" + x).retrieve().bodyToMono(String.class).block();

        return Integer.decode(response) + 42;
    }

    @GetMapping("/cat42inc/{x}")
    int cat42inc(@PathVariable("x") int x) {
        WebClient client = WebClient.create("http://localhost:8080");
        String response = client.get().uri("/inc/" + (42 + x)).retrieve().bodyToFlux(String.class).blockFirst();
        return Integer.decode(response);
    }
}
