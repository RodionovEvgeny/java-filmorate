package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbGenreStorage implements GenreStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM \"Genres\"", this::createGenre);
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from \"Genres\" where \"Genre_id\" = ?", id);

        if (rows.next()) {
            return new Genre(rows.getInt("Genre_id"), FilmGenres.valueOf(rows.getString("Genre_name")));
        } else {
            throw new GenreNotFoundException(String.format("Жанр с id %s не найден.", id));
        }
    }

    private Genre createGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(
                resultSet.getInt("Genre_id"),
                FilmGenres.valueOf(resultSet.getString("Genre_name")));
    }
}
