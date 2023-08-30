package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class DbFilmStorageTest {

    private final Film film1 = Film.builder()
            .name("Name1")
            .description("Description1")
            .duration(15)
            .releaseDate(LocalDate.parse("2000-01-01"))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film film2 = Film.builder()
            .name("Name2")
            .description("Description2")
            .duration(20)
            .releaseDate(LocalDate.parse("2001-01-01"))
            .mpa(new Mpa(2, null))
            .genres(List.of(new Genre(2, null)))
            .build();

    private final User user1 = new User("1email@email.com",
            "login1", LocalDate.now().minusYears(20), 0, "Name1");
    private final User user2 = new User("2email@email.com",
            "login2", LocalDate.now().minusYears(20), 0, "Name2");

    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;

    @AfterEach
    void tearDown() {
        filmStorage.deleteAllFilms();
    }

    @Test
    void hasToAddFilm() {
        Film film = filmStorage.addFilm(film1);

        assertThat(film).hasFieldOrPropertyWithValue("name", film1.getName());
        assertThat(film).hasFieldOrPropertyWithValue("description", film1.getDescription());
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", film1.getReleaseDate());
        assertThat(film).hasFieldOrPropertyWithValue("duration", film1.getDuration());
        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));
        assertThat(film).hasFieldOrPropertyWithValue("genres", List.of(new Genre(1, "Комедия")));
    }

    @Test
    void hasToUpdateFilm() {
        int id = filmStorage.addFilm(film1).getId();
        film2.setId(id);
        Film film = filmStorage.updateFilm(film2);

        assertThat(film).hasFieldOrPropertyWithValue("id", id);
        assertThat(film).hasFieldOrPropertyWithValue("name", film2.getName());
        assertThat(film).hasFieldOrPropertyWithValue("description", film2.getDescription());
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", film2.getReleaseDate());
        assertThat(film).hasFieldOrPropertyWithValue("duration", film2.getDuration());
        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG"));
        assertThat(film).hasFieldOrPropertyWithValue("genres", List.of(new Genre(2, "Драма")));
        assertThat(filmStorage.getAllFilms()).hasSize(1);
    }

    @Test
    void hasToGetAllFilms() {
        filmStorage.addFilm(film1);
        assertThat(filmStorage.getAllFilms()).hasSize(1);
        filmStorage.addFilm(film2);
        List<Film> films = filmStorage.getAllFilms();

        assertThat(films).hasSize(2);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", film1.getName());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", film1.getDescription());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("releaseDate", film1.getReleaseDate());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", film1.getDuration());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("genres", List.of(new Genre(1, "Комедия")));

        assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", film2.getName());
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", film2.getDescription());
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("releaseDate", film2.getReleaseDate());
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", film2.getDuration());
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("mpa", new Mpa(2, "PG"));
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("genres", List.of(new Genre(2, "Драма")));
    }

    @Test
    void hasToGetFilmById() {
        int id = filmStorage.addFilm(film1).getId();
        Film film = filmStorage.getFilmById(id);

        assertThat(film).hasFieldOrPropertyWithValue("id", id);
        assertThat(film).hasFieldOrPropertyWithValue("name", film1.getName());
        assertThat(film).hasFieldOrPropertyWithValue("description", film1.getDescription());
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate", film1.getReleaseDate());
        assertThat(film).hasFieldOrPropertyWithValue("duration", film1.getDuration());
        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));
        assertThat(film).hasFieldOrPropertyWithValue("genres", List.of(new Genre(1, "Комедия")));
    }

    @Test
    void hasToAddLike() {
        int filmId = filmStorage.addFilm(film1).getId();
        int userId = userStorage.addUser(user1).getId();

        filmStorage.addLike(filmId, userId);
        List<Integer> likes = filmStorage.getLikesByFilmId(filmId);
        assertThat(likes).hasSize(1);
        assertThat(likes.get(0)).isEqualTo(userId);
    }

    @Test
    void hasToDeleteLike() {
        int filmId = filmStorage.addFilm(film1).getId();
        int userId = userStorage.addUser(user1).getId();

        filmStorage.addLike(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
        List<Integer> likes = filmStorage.getLikesByFilmId(filmId);
        assertThat(likes).hasSize(0);
    }

    @Test
    void hasToGetTopFilms() {
        int firstFilmId = filmStorage.addFilm(film1).getId();
        int secondFilmId = filmStorage.addFilm(film2).getId();
        int firstUserId = userStorage.addUser(user1).getId();
        int secondUserId = userStorage.addUser(user2).getId();

        filmStorage.addLike(firstFilmId, firstUserId);
        filmStorage.addLike(secondFilmId, firstUserId);
        filmStorage.addLike(secondFilmId, secondUserId);

        List<Film> topFilms = filmStorage.getTopFilms(10);
        assertThat(topFilms).hasSize(2);
        assertThat(topFilms.get(0)).hasFieldOrPropertyWithValue("id", secondFilmId);
        assertThat(topFilms.get(1)).hasFieldOrPropertyWithValue("id", firstFilmId);

        List<Film> topFilm = filmStorage.getTopFilms(1);
        assertThat(topFilm).hasSize(1);
        assertThat(topFilm.get(0)).hasFieldOrPropertyWithValue("id", secondFilmId);
    }
}