package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int vacantId = 1;

    @Override
    public User addUser(User user) {
        if (user.getId() == 0) user.setId(vacantId++);
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен. Текущее количество пользователей {}", users.size());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!(user.getId() == 0 || users.containsKey(user.getId()))) {
            log.warn("Пользователь с id = {} не найден.", user.getId());
            throw new ValidationException("Пользователь с такиим id не найден.");
        }
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} обновлены.", user.getLogin());
        return user;
    }

    @Override
    public Set<User> getAllUsers() {
        log.debug("Список всех зарегестрированных пользователей отправлен.");
        return new HashSet<>(users.values());
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        vacantId = 1;
    }
}
