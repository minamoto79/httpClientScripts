package com.example;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/payments")
public class PaymentsController {
    @Inject
    @Client("http://localhost:8081")
    HttpClient httpClient;

    @Get
    public List<Payment> getAll() {
        HttpRequest<?> req = HttpRequest.GET("/products");

        List<Product> response = httpClient.toBlocking().retrieve(req, Argument.listOf(Product.class));

        return response.stream()
                .map(p -> new Payment(p.getId(), "10$"))
                .collect(Collectors.toList());
    }
}

class Product {
    private String id;

    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

class Payment {

    String product;
    String cost;

    public Payment(String product, String cost) {
        this.product = product;
        this.cost = cost;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}