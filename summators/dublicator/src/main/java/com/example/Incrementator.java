package com.example;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="incrementator", url = "http://localhost:8080/")
public interface Incrementator {
    @GetMapping("/inc/{x}")
    int inc(@PathVariable("x") int x);
}
