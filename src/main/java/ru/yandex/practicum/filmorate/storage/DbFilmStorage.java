package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

            // Film_id Name Description Duration Release_date Rating_id Genre_id
            return stmt;
        }, keyHolder);

        int newId = keyHolder.getKey().intValue();
        film.setId(newId);

        log.debug("Фильм добавлен.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!(film.getId() == 0 || films.containsKey(film.getId()))) {
            log.warn("Фильм с id = {} не найден.", film.getId());
            throw new ValidationException("Фильм с такиим id не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Данные фильма {} обновлены.", film.getName());
        return film;
    }

    @Override
    public Set<Film> getAllFilms() {
        log.debug("Список всех фильмов отправлен.");
        return new HashSet<>(films.values());
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        nextVacantId = 1;
    }

    @Override
    public Film getFilmById(Integer id) {
        if (films.containsKey(id)) return films.get(id);
        else {
            log.warn(String.format("Фильм с id %s не найден.", id));
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден.", id));
        }
    }
}
