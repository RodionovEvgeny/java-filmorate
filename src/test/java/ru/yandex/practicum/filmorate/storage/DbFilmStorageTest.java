package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class DbFilmStorageTest {

    private final Film film1 = Film.builder()
            .name("Name1")
            .description("Description1")
            .duration(15)
            .releaseDate(LocalDate.of(1900, 1, 1))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film film2 = Film.builder()
            .name("Name2")
            .description("Description2")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(2, null))
            .genres(List.of(new Genre(2, null)))
            .build();
    private final Film film3 = Film.builder()
            .name("Name3")
            .description("Description3")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(3, null))
            .genres(List.of(new Genre(3, null)))
            .build();
    private final User user1 = new User("1email@email.com",
            "login", LocalDate.now().minusYears(20), 0, "Name1");
    private final User user2 = new User("2email@email.com",
            "login", LocalDate.now().minusYears(20), 0, "Name2");
    private final User user3 = new User("3email@email.com",
            "login", LocalDate.now().minusYears(20), 0, "Name3");
    @Autowired
    private FilmStorage filmStorage;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void addFilm() {
        int id = filmStorage.addFilm(film1).getId();
        Film film = filmStorage.getFilmById(id);
        Date date = java.sql.Date.valueOf(film1.getReleaseDate());

        assertThat(film).hasFieldOrPropertyWithValue("name", film1.getName());
        assertThat(film).hasFieldOrPropertyWithValue("description", film1.getDescription());
        assertThat(film).hasFieldOrPropertyWithValue("releaseDate",LocalDate.of(1900, 1, 1));
        assertThat(film).hasFieldOrPropertyWithValue("duration", film1.getDuration());
        assertThat(film).hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"));
        assertThat(film).hasFieldOrPropertyWithValue("genres", List.of(new Genre(1, "Комедия")));
    }

    @Test
    void updateFilm() {
    }

    @Test
    void getAllFilms() {
    }

    @Test
    void getFilmById() {
    }

    @Test
    void addLike() {
    }

    @Test
    void deleteLike() {
    }

    @Test
    void getTopFilms() {
    }
}