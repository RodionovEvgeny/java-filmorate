package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class DbFilmStorage implements FilmStorage {
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
                                    "VALUES (?, ?, ?, ?, ?);",
                            new String[]{"Film_id"});
                    stmt.setString(1, film.getName());
                    stmt.setString(2, film.getDescription());
                    stmt.setInt(3, film.getDuration());
                    stmt.setDate(4, java.sql.Date.valueOf(film.getReleaseDate()));
                    stmt.setInt(5, film.getMpa().getId());
                    return stmt;
                },
                keyHolder);
        int newId = keyHolder.getKey().intValue();
        film.setId(newId);
        updateGenresDb(film);
        log.debug("Фильм добавлен.");
        return getFilmById(newId);
    }

    private void updateGenresDb(Film film) {
        jdbcTemplate.update("DELETE FROM  \"Film_genres\" WHERE \"Film_id\" = ?", film.getId());
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update("MERGE INTO  \"Film_genres\" (\"Film_id\", \"Genre_id\") VALUES (?, ?)",
                        film.getId(),
                        genre.getId());
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        String sqlQuery = "UPDATE \"Film\" " +
                "SET \"Name\" = ?, \"Description\" = ?, \"Duration\" = ?, \"Release_date\" = ?, \"Rating_id\" = ? " +
                "WHERE \"Film_id\" = ?";
        updateGenresDb(film);
        int updateStatus = jdbcTemplate.update(
                sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        if (updateStatus == 0) {
            throw new FilmNotFoundException(String.format("Фильм с id = %s не найден.", film.getId()));
        }
        return getFilmById(film.getId());
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * " +
                "FROM \"Film\" AS f " +
                "JOIN \"Rating\" AS r ON f.\"Rating_id\"=r.\"Rating_id\"";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(Integer id) {
        try {
            String sqlQuery = "SELECT * " +
                    "FROM \"Film\" AS f " +
                    "JOIN \"Rating\" AS r ON f.\"Rating_id\"=r.\"Rating_id\" " +
                    "WHERE \"Film_id\" = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw new FilmNotFoundException(String.format("Фильм с id %s не найден.", id));
        }
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO  \"Likes\" (\"Film_id\", \"User_id\") VALUES (?,?)", filmId, userId);
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
                "(SELECT \"Film_id\", COUNT(\"User_id\") as ct FROM \"Likes\" GROUP BY \"Film_id\") " +
                "AS fc ON fc.\"Film_id\" = f.\"Film_id\" " +
                "ORDER BY fc.ct DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public void deleteAllFilms() {
        jdbcTemplate.update("DELETE FROM \"Film_genres\"");
        jdbcTemplate.update("DELETE FROM \"Likes\"");
        jdbcTemplate.update("DELETE FROM \"Film\"");
    }

    private List<Genre> getFilmsGenresById(int filmId) {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * " +
                        "FROM \"Film_genres\" AS fg " +
                        "JOIN \"Genres\" AS g ON fg.\"Genre_id\"=g.\"Genre_id\" " +
                        "WHERE \"Film_id\" = ?",
                filmId);
        while (rowSet.next()) {
            Genre genre = new Genre(rowSet.getInt("Genre_id"), rowSet.getString("Genre_name"));
            genres.add(genre);
        }
        return genres;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .name(resultSet.getString("Name"))
                .description(resultSet.getString("Description"))
                .duration(resultSet.getInt("Duration"))
                .releaseDate(resultSet.getDate("Release_date").toLocalDate())
                .id(resultSet.getInt("Film_id"))
                .mpa(new Mpa(resultSet.getInt("Rating_id"), resultSet.getString("Rating_name")))
                .genres(getFilmsGenresById(resultSet.getInt("Film_id")))
                .build();
    }

    public List<Integer> getLikesByFilmId(int id) {
        return jdbcTemplate.query("SELECT * FROM \"Likes\" WHERE \"Film_id\" = ?",
                (rs, rn) -> rs.getInt("User_id"), id);
    }
}