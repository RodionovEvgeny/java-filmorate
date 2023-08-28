package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component("dbUserStorage")
@Primary
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
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

        log.debug("Пользователь добавлен.");
        return user;
    }

    @Override
    public User updateUser(User user) {

        String sqlQuery = "update \"User\" set " +
                "\"Name\" = ?, \"Login\" = ?, \"Email\" = ?, \"Birthday\" = ? " +
                "where \"User_id\" = ?";
        jdbcTemplate.update(sqlQuery
                , user.getName()
                , user.getLogin()
                , user.getEmail()
                , user.getBirthday()
                , user.getId());
        return getUserById(user.getId());



        /*if (!(user.getId() == 0 || users.containsKey(user.getId()))) {
            log.warn("Пользователь с id = {} не найден.", user.getId());
            throw new ValidationException("Пользователь с такиим id не найден.");
        }
        users.put(user.getId(), user);
        log.debug("Данные пользователя {} обновлены.", user.getLogin());
        return user;*/
    }

    @Override
    public Set<User> getAllUsers() {
        String sqlQuery = "select * from \"User\"";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser));
        /*log.debug("Список всех зарегестрированных пользователей отправлен.");
        return new HashSet<>(users.values());*/

    }

    @Override
    public void deleteAllUsers() {
        String sqlQuery = "delete from \"User\"";
        jdbcTemplate.update(sqlQuery); // TODO сделать бы так, чтобы после удаления всех пользователей id с 1 опять шли
    }

    @Override
    public User getUserById(Integer id) {
        try {
            String sqlQuery = "select * " +
                    "from \"User\" where \"User_id\" = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден.", id));
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getString("Email"),
                (resultSet.getString("Login")),
                ((resultSet.getDate("Birthday"))).toLocalDate(),
                (resultSet.getInt("User_id")),
                (resultSet.getString("Name")));
    }

    @Override
    public void addFriends(int firstUserId, int secondUserId) { // TODO базово работает. Но надо проверку на наличие пользователя делать

        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT * FROM \"Friends\" WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                firstUserId,
                secondUserId
        );
        if (row.next()) {
            throw new RuntimeException(String.format( // TODO new exception for friendship
                    "Пользователь с id %s уже добавлен в друзья к пользователю с id %s",
                    firstUserId,
                    secondUserId
            ));
        }

        row = jdbcTemplate.queryForRowSet(
                "SELECT * FROM \"Friends\" WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                secondUserId,
                firstUserId
        );

        if (row.next()) {
            jdbcTemplate.update(
                    "INSERT INTO \"Friends\" (\"User_id\", \"Friend_id\", \"Approved\" ) VALUES (?, ?, ?)",
                    firstUserId,
                    secondUserId,
                    true
            );
            jdbcTemplate.update(
                    "UPDATE \"Friends\" SET  \"Approved\" = ?  WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                    true,
                    secondUserId,
                    firstUserId
            );
            return;
        }

        jdbcTemplate.update(
                "INSERT INTO \"Friends\" (\"User_id\", \"Friend_id\", \"Approved\" ) VALUES (?, ?, ?)",
                firstUserId,
                secondUserId,
                false
        );
    }

    @Override
    public List<User> getUsersFriends(int userId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT u.\"User_id\" ,u.\"Name\" ,u.\"Login\" ,u.\"Email\" ,u.\"Birthday\" \n" +
                        "FROM \"Friends\" as f\n" +
                        "JOIN \"User\" as u on u.\"User_id\" = f.\"Friend_id\"\n" +
                        "WHERE f.\"User_id\" = ?", userId);
        while (row.next()) {
            User user = new User(
                    row.getString("Email"),
                    (row.getString("Login")),
                    ((row.getDate("Birthday"))).toLocalDate(),
                    (row.getInt("User_id")),
                    (row.getString("Name")));
            friends.add(user);
        }
        return friends;
    }

    @Override
    public List<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {

        List<User> friends = new ArrayList<>();
        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT u.\"User_id\" ,u.\"Name\" ,u.\"Login\" ,u.\"Email\" ,u.\"Birthday\"\n" +
                        "FROM \"Friends\" as f\n" +
                        "JOIN \"User\" as u on u.\"User_id\" = f.\"Friend_id\"\n" +
                        "WHERE f.\"User_id\" = ? AND f.\"Friend_id\" IN (\n" +
                        "SELECT \"Friend_id\"\n" +
                        "FROM \"Friends\"\n" +
                        "WHERE \"User_id\" = ?\n" +
                        ")",
                firstUserId, secondUserId);

        while (row.next()) {
            User user = new User(
                    row.getString("Email"),
                    (row.getString("Login")),
                    ((row.getDate("Birthday"))).toLocalDate(),
                    (row.getInt("User_id")),
                    (row.getString("Name")));
            friends.add(user);
        }
        return friends;
    }

    @Override
    public void deleteFriend(Integer firstUserId, Integer secondUserId) {
        jdbcTemplate.update(
                "DELETE FROM \"Friends\"  WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                firstUserId,
                secondUserId);
        jdbcTemplate.update(
                "UPDATE \"Friends\" SET  \"Approved\" = ?  WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                false,
                secondUserId,
                firstUserId
        );
    }
}
