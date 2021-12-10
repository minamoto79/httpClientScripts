package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class IncrementorController {
    @GetMapping("/inc/{x}")
    int inc(@PathVariable("x") int x) {
        return x + 1;
    }

    @GetMapping("/inc2loc/{x}")
    int incAndLocDouble(@PathVariable("x") int x) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity("http://localhost:8081/dub/" + inc(x), String.class);
        return Integer.decode(response.getBody());
    }
}
