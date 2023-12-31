package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

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
            throw new EntityNotFoundException(String.format("Рейтинг с id %s не найден.", id), Mpa.class.getName());
        }
    }

    private Mpa createMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(
                resultSet.getInt("Rating_id"),
                resultSet.getString("Rating_name"));
    }
}
