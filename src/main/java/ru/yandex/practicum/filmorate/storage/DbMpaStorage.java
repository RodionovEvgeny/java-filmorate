package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbMpaStorage implements MpaStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM \"Rating\"", this::createMpa);
    }

    @Override
    public Mpa getMpaById(int id) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from \"Rating\" where \"Rating_id\" = ?", id);

        if (rows.next()) {
            return new Mpa(rows.getInt("Rating_id"), rows.getString("Rating_name"));
        } else {
            throw new MpaNotFoundException(String.format("Рейтинг с id %s не найден.", id));
        }
    }

    private Mpa createMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(
                resultSet.getInt("Rating_id"),
                resultSet.getString("Rating_name"));
    }
}
