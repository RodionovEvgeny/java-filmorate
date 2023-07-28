package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserStorage userStorage;

    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    @PostMapping
    private User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping
    private User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping
    private Set<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
