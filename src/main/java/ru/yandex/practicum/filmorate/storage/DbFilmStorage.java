package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@Primary
public class DbFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextVacantId = 1;
    JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO \"Film\" ( " +
                            " \"Name\" ," +
                            " \"Description\"," +
                            "\"Duration\"," +
                            " \"Release_date\"," +
                            " \"Rating_id\" )" +
                            "VALUES (?, ?, ?, ?, ?);", new String[]{"Film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5,film.getFilmRatingId());
            return stmt;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        film.setId(newId);

        log.debug("Фильм добавлен.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {// TODO капсом написать запросы все

        String sqlQuery = "UPDATE \"Film\" SET \"Name\" = ?," +
                " \"Description\" = ?, \"Duration\" = ?," +
                " \"Release_date\" = ?, \"Rating_id\" = ? WHERE \"Film_id\" = ?";

        /*if (film.getGenres() != null && film.getGenres().size() != 0) {
            film.setGenres(setGenres(film));
        } else {
            film.setGenres(Collections.emptySet());
            jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());
        }*/

        int updateStatus = jdbcTemplate.update(sqlQuery,
                film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getFilmRatingId(), film.getId());

        if (updateStatus == 0) {
            throw new FilmNotFoundException("Фильм с id = " + film.getId() + " не найден.");
        }
        return film;
    }

    @Override
    public Set<Film> getAllFilms() {
        String sqlQuery = "select * from \"Film\"";
        return new HashSet<>(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        nextVacantId = 1;
    }

    @Override
    public Film getFilmById(Integer id) {
        try {
            String sqlQuery = "select * " +
                    "from \"Film\" where \"Film_id\" = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден.", id));
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getString("Name"),
                (resultSet.getString("Description")),
                (resultSet.getInt("Duration")),
                ((resultSet.getDate("Release_date"))).toLocalDate());
        film.setFilmRating(Film.getFilmRatingById(resultSet.getInt("Rating_id")));
        film.setId(resultSet.getInt("Film_id"));
                return film;
    }
}
