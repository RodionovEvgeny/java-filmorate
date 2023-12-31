package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM \"Genres\"", this::createGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from \"Genres\" where \"Genre_id\" = ?", id);

        if (rows.next()) {
            return new Genre(rows.getInt("Genre_id"), rows.getString("Genre_name"));
        } else {
            throw new EntityNotFoundException(String.format("Жанр с id %s не найден.", id), Genre.class.getName());
        }
    }

    private Genre createGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("Genre_id"),
                resultSet.getString("Genre_name"));
    }
}
