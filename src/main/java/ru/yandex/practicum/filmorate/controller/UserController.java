package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @DeleteMapping
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @PostMapping
    private User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    private User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    private Set<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    private User getUser(@PathVariable(name = "id") Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    private List<User> getUsersFriends(@PathVariable(name = "id") Integer id) {
        return userService.getUsersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    private Set<User> getMutualFriends(@PathVariable(name = "id") Integer id,
                                       @PathVariable(name = "otherId") Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    private void addToFriends(@PathVariable(name = "id") Integer id,
                              @PathVariable(name = "friendId") Integer friendId) {
        userService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    private void deleteFriends(@PathVariable(name = "id") Integer id,
                               @PathVariable(name = "friendId") Integer friendId) {
        userService.deleteFriend(id, friendId);
    }
}
