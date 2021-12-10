package com.example;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Import(FeignClientsConfiguration.class)
@RestController
public class DublicatorController {
    private Incrementator incer;

    @Autowired
    DublicatorController(Decoder decoder,
                         Encoder encoder,
                         //Client client,
                         Contract contract) {
        incer = Feign
                .builder()
                //.client(client)
                .contract(contract)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .target(Incrementator.class, "http://localhost:8080");

    }
    @GetMapping("/dub/{x}")
    int dub(@PathVariable("x") int x) {
        return 2 * x;
    }

    @GetMapping("/dub2loc/{x}")
    int incAndLocDouble(@PathVariable("x") int x) {
        return inc(dub(x));
    }

    @GetMapping("/dubInc/{x}")
    int dubInc(@PathVariable("x") int x) {
        return dub(incer.inc(x));
    }

    @GetMapping("/incDub/{x}")
    int incDub(@PathVariable("x") int x) {
        return incer.inc(dub(x));
    }

    int inc(int x) {
        return x + 1;
    }
}
