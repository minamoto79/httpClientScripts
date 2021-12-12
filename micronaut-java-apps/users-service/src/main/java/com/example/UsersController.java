package com.example;

import io.micronaut.http.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Controller("/users")
public class UsersController {
    private final List<User> usersDb = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong idGenerator = new AtomicLong();

    @Get
    public List<User> getAll() {
        return new ArrayList<>(usersDb);
    }

    @Get("/{id}")
    public User get(@PathVariable("id") String id) {
        return usersDb.stream()
                .filter(u -> Objects.equals(u.getId(), id))
                .findFirst()
                .orElse(null);
    }

    @Post
    public User post(@Body UserCreateData userData) {
        User user = new User();
        user.setName(userData.getName());
        user.setId("user-" + idGenerator.incrementAndGet());

        usersDb.add(user);
        return user;
    }

    @Delete("/{id}")
    public void delete(@PathVariable("id") String id) {
        usersDb.removeIf(u -> Objects.equals(u.getId(), id));
    }
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