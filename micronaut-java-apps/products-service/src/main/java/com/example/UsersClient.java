package com.example;

import io.micronaut.http.annotation.*;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client(id = "http://localhost:8080", path = "/users")
public interface UsersClient {
    @Get
    List<User> getAll();

    @Get("/{id}")
    User get(@PathVariable("id") String id);

    @Post
    User create(@Body UserCreateData userData);

    @Delete("/{id}")
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