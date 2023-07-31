package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class UserController {

    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    @PostMapping("/users")
    private User addUser(@Valid @RequestBody User user) {
        return userStorage.addUser(user);
    }

    @PutMapping("/users")
    private User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @GetMapping("/users")
    private Set<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping(("/users/{id}"))
    private User getUser(@PathVariable(name = "id") Integer id) {
        return userStorage.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    private void addToFriends(@PathVariable Map<String, String> ids) {
        userService.addToFriends(ids.get("id"),ids.get("friendId"));
    }
}
