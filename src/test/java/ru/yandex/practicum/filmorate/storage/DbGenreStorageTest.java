package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class DbGenreStorageTest {
    @Autowired
    private GenreStorage genreStorage;

    @Test
    void getAllGenres() {
        List<Genre> genres = genreStorage.getAllGenres();

        assertThat(genres).hasSize(6);
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(genres.get(0)).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genres.get(1)).hasFieldOrPropertyWithValue("id", 2);
        assertThat(genres.get(1)).hasFieldOrPropertyWithValue("name", "Драма");
        assertThat(genres.get(2)).hasFieldOrPropertyWithValue("id", 3);
        assertThat(genres.get(2)).hasFieldOrPropertyWithValue("name", "Мультфильм");
        assertThat(genres.get(3)).hasFieldOrPropertyWithValue("id", 4);
        assertThat(genres.get(3)).hasFieldOrPropertyWithValue("name", "Триллер");
        assertThat(genres.get(4)).hasFieldOrPropertyWithValue("id", 5);
        assertThat(genres.get(4)).hasFieldOrPropertyWithValue("name", "Документальный");
        assertThat(genres.get(5)).hasFieldOrPropertyWithValue("id", 6);
        assertThat(genres.get(5)).hasFieldOrPropertyWithValue("name", "Боевик");
    }

    @Test
    void getGenreById() {
        assertThat(genreStorage.getGenreById(1)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(genreStorage.getGenreById(1)).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(genreStorage.getGenreById(6)).hasFieldOrPropertyWithValue("id", 6);
        assertThat(genreStorage.getGenreById(6)).hasFieldOrPropertyWithValue("name", "Боевик");
    }
}