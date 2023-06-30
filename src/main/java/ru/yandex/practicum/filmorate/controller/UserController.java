package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

public class UserController {

    private Set<User> users = new HashSet<>();

    @PostMapping("/user")
    private User addUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @PutMapping("/user")
    private User updateUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @GetMapping("/users")
    private Set<User> getAllUsers() {
        return users;
    }


}
