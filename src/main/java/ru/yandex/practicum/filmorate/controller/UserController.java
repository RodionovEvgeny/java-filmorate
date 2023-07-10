package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Map<Integer, User> users = new HashMap<>();
    private static int nextVacantId = 1;

    public static Map<Integer, User> getUsers() {
        return users;
    }

    public static void deleteAllUsers() {
        users.clear();
        nextVacantId = 1;
    }

    @PostMapping
    private User addUser(@Valid @RequestBody User user) {
        if (user.getId() == 0) user.setId(nextVacantId++);
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен. Текущее количество пользователей {}", users.size());
        return user;
    }

    @PutMapping
    private User updateUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} обновлены.", user.getLogin());
        return user;
    }

    @GetMapping
    private Set<User> getAllUsers() {
        log.debug("Список всех зарегестрированных пользователей отправлен.");
        return new HashSet<>(users.values());
    }
}
