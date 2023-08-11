package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.*;

@Slf4j
@Component("dbUserStorage")
@Primary
public class DbUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final JdbcTemplate jdbcTemplate;
    private int vacantId = 1;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        if (user.getId() == 0) user.setId(vacantId++);
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO \"User\" ( \"Name\" , \"Login\" , \"Email\", \"Birthday\") " +
                    "VALUES (?, ?, ?, ?);", new String[]{"User_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        user.setId(newId);
        /*jdbcTemplate.update("INSERT INTO \"User\" ( \"Name\" , \"Login\" , \"Email\", \"Birthday\") " +
                        "VALUES (?, ?, ?, ?);",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday()
        );*/

        log.debug("Пользователь добавлен.");
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

    @Override
    public User getUserById(Integer id) {
        if (users.containsKey(id)) return users.get(id);
        else {
            log.warn(String.format("Пользователь с id %s не найден.", id));
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден.", id));
        }
    }
}
