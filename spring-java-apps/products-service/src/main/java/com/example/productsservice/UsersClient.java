package com.example.productsservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(url = "http://localhost:8080/users")
public interface UsersClient {
    @GetMapping
    List<User> getAll();

    @GetMapping("/{id}")
    User get(@PathVariable("id") String id);

    @PostMapping
    User create(@RequestBody UserCreateData userData);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") String id);
}

class UserCreateData {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class User {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}