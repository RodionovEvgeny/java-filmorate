package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@Primary
public class DbFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    JdbcTemplate jdbcTemplate;
    private int nextVacantId = 1;

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
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        int newId = keyHolder.getKey().intValue();
        film.setId(newId);

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO  \"Film_genres\" (\"Film_id\", \"Genre_id\") VALUES (?, ?)",
                        film.getId(),
                        genre.getId());
            }
        }


        log.debug("Фильм добавлен.");
        return film;
    }

    @Override
    public Film updateFilm(Film film) {// TODO капсом написать запросы все

        String sqlQuery = "UPDATE \"Film\" SET \"Name\" = ?," +
                " \"Description\" = ?, \"Duration\" = ?," +
                " \"Release_date\" = ?, \"Rating_id\" = ? WHERE \"Film_id\" = ?";

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("INSERT INTO  \"Film_genres\" (\"Film_id\", \"Genre_id\") VALUES (?, ?)",
                        film.getId(),
                        genre.getId());
            }
        } else {
            jdbcTemplate.update("DELETE FROM  \"Film_genres\" WHERE \"Film_id\" = ?", film.getId());
        }
        int updateStatus = jdbcTemplate.update(sqlQuery,
                film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId(), film.getId());

        if (updateStatus == 0) {
            throw new FilmNotFoundException("Фильм с id = " + film.getId() + " не найден.");
        }
        return film;
    }

    @Override
    public Set<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM \"Film\" AS f JOIN \"Rating\" AS r ON f.\"Rating_id\"=r.\"Rating_id\"";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        //for (Film film : films) {
        //    film.setGenres(getFilmsGenresById(film.getId()));
        // }
        return new HashSet<>(films);
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        nextVacantId = 1;
    }

    @Override
    public Film getFilmById(Integer id) {
        try {
            String sqlQuery = "SELECT * " +
                    "FROM \"Film\" AS f " +
                    "JOIN \"Rating\" AS r ON f.\"Rating_id\"=r.\"Rating_id\" " +
                    "WHERE \"Film_id\" = ?";
            Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);

            //film.setGenres(getFilmsGenresById(id));

            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден.", id));
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        try {
            jdbcTemplate.update("INSERT INTO  \"Likes\" (\"Film_id\", \"User_id\") VALUES (?,?)", filmId, userId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ororor");// TODO Сделать нормальное исключение или удалить его вовсе
        }

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM  \"Likes\" WHERE \"Film_id\" = ? AND \"User_id\" = ?", filmId, userId);
    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT * " +
                "FROM \"Film\" AS f " +
                "JOIN \"Rating\" AS r ON f.\"Rating_id\"=r.\"Rating_id\" " +
                "LEFT JOIN " +
                "(SELECT \"Film_id\", COUNT(\"User_id\") as ct FROM \"Likes\" GROUP BY \"Film_id\" ORDER BY ct) " +
                "AS fc ON fc.\"Film_id\" = f.\"Film_id\" " +
                "ORDER BY fc.ct " +
                "LIMIT ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);

        return films;
    }

    private Set<Genre> getFilmsGenresById(int filmId) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM \"Film_genres\" AS fg " +
                "JOIN \"Genres\" AS g ON fg.\"Genre_id\"=g.\"Genre_id\" " +
                "WHERE \"Film_id\" = ?", filmId);

        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("Genre_id"), rowSet.getString("Genre_name"));
            genres.add(genre);
        }
        return genres;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film(
                resultSet.getString("Name"),
                (resultSet.getString("Description")),
                (resultSet.getInt("Duration")),
                ((resultSet.getDate("Release_date"))).toLocalDate());
        film.setMpa(new Mpa(resultSet.getInt("Rating_id"), resultSet.getString("Rating_name")));
        film.setId(resultSet.getInt("Film_id"));
        film.setGenres(getFilmsGenresById(film.getId()));
        return film;
    }


}