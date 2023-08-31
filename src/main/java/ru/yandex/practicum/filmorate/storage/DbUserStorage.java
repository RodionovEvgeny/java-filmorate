package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component("dbUserStorage")
@RequiredArgsConstructor
public class DbUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isEmpty())
            user.setName(user.getLogin());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(
                            "INSERT INTO \"User\" ( \"Name\" , \"Login\" , \"Email\", \"Birthday\") " +
                                    "VALUES (?, ?, ?, ?);",
                            new String[]{"User_id"});
                    stmt.setString(1, user.getName());
                    stmt.setString(2, user.getLogin());
                    stmt.setString(3, user.getEmail());
                    stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
                    return stmt;
                },
                keyHolder);
        int newId = keyHolder.getKey().intValue();
        user.setId(newId);
        log.debug("Пользователь добавлен.");
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE \"User\" " +
                "SET \"Name\" = ?, \"Login\" = ?, \"Email\" = ?, \"Birthday\" = ? " +
                "WHERE \"User_id\" = ?";
        int updateStatus = jdbcTemplate.update(
                sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (updateStatus == 0) {
            throw new EntityNotFoundException(
                    String.format("Пользователь с id = %s не найден.", user.getId()), User.class.getName());
        }
        return getUserById(user.getId());
    }

    @Override
    public Set<User> getAllUsers() {
        return new HashSet<>(jdbcTemplate.query("SELECT * FROM \"User\"", this::mapRowToUser));
    }

    @Override
    public void deleteAllUsers() {
        jdbcTemplate.update("DELETE FROM \"Friends\"");
        jdbcTemplate.update("DELETE FROM \"User\"");
    }

    @Override
    public User getUserById(Integer id) {
        try {
            String sqlQuery = "SELECT * " +
                    "FROM \"User\" " +
                    "WHERE \"User_id\" = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException(
                    String.format("Пользователь с id %s не найден.", id), User.class.getName());
        }
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(
                resultSet.getInt("User_id"),
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                resultSet.getDate("Birthday").toLocalDate(),
                resultSet.getString("Name"));
    }

    @Override
    public void addFriends(int firstUserId, int secondUserId) {
        validateFriendship(firstUserId, secondUserId);

        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM \"Friends\" " +
                        "WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
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

    private void validateFriendship(int firstUserId, int secondUserId) {
        getUserById(firstUserId);
        getUserById(secondUserId);
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT * " +
                        "FROM \"Friends\" " +
                        "WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                firstUserId,
                secondUserId
        );
        if (row.next()) {
            throw new RuntimeException(String.format(
                    "Пользователь с id %s уже добавлен в друзья к пользователю с id %s",
                    firstUserId,
                    secondUserId
            ));
        }
    }

    @Override
    public List<User> getUsersFriends(int userId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT u.\"User_id\" ,u.\"Name\" ,u.\"Login\" ,u.\"Email\" ,u.\"Birthday\" \n" +
                        "FROM \"Friends\" AS f\n" +
                        "JOIN \"User\" AS u ON u.\"User_id\" = f.\"Friend_id\"\n" +
                        "WHERE f.\"User_id\" = ? ", userId);
        while (row.next()) {
            User user = new User(
                    (row.getInt("User_id")),
                    row.getString("Email"),
                    (row.getString("Login")),
                    ((row.getDate("Birthday"))).toLocalDate(),
                    (row.getString("Name")));
            friends.add(user);
        }
        friends.sort(Comparator.comparingInt(User::getId));
        return friends;
    }

    @Override
    public List<User> getMutualFriends(Integer firstUserId, Integer secondUserId) {
        List<User> friends = new ArrayList<>();
        SqlRowSet row = jdbcTemplate.queryForRowSet(
                "SELECT u.\"User_id\" ,u.\"Name\" ,u.\"Login\" ,u.\"Email\" ,u.\"Birthday\"" +
                        "FROM \"Friends\" AS f " +
                        "JOIN \"User\" AS u ON u.\"User_id\" = f.\"Friend_id\"" +
                        "WHERE f.\"User_id\" = ? AND f.\"Friend_id\" IN (" +
                        "SELECT \"Friend_id\"" +
                        "FROM \"Friends\"" +
                        "WHERE \"User_id\" = ?)",
                firstUserId,
                secondUserId);

        while (row.next()) {
            User user = new User(
                    (row.getInt("User_id")),
                    row.getString("Email"),
                    (row.getString("Login")),
                    ((row.getDate("Birthday"))).toLocalDate(),
                    (row.getString("Name")));
            friends.add(user);
        }
        return friends;
    }

    @Override
    public void deleteFriend(Integer firstUserId, Integer secondUserId) {
        jdbcTemplate.update(
                "DELETE FROM \"Friends\" WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                firstUserId,
                secondUserId);
        jdbcTemplate.update(
                "UPDATE \"Friends\" SET \"Approved\" = ? WHERE \"User_id\" = ? AND \"Friend_id\" = ?",
                false,
                secondUserId,
                firstUserId
        );
    }
}
