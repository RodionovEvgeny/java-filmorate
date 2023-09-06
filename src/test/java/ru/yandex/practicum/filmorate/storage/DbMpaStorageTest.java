package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
class DbMpaStorageTest {
    @Autowired
    private MpaStorage mpaStorage;

    @Test
    void hasToGetAllMpa() {
        List<Mpa> mpaList = mpaStorage.getAllMpa();

        assertThat(mpaList).hasSize(5);
        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(mpaList.get(0)).hasFieldOrPropertyWithValue("name", "G");
        assertThat(mpaList.get(1)).hasFieldOrPropertyWithValue("id", 2);
        assertThat(mpaList.get(1)).hasFieldOrPropertyWithValue("name", "PG");
        assertThat(mpaList.get(2)).hasFieldOrPropertyWithValue("id", 3);
        assertThat(mpaList.get(2)).hasFieldOrPropertyWithValue("name", "PG-13");
        assertThat(mpaList.get(3)).hasFieldOrPropertyWithValue("id", 4);
        assertThat(mpaList.get(3)).hasFieldOrPropertyWithValue("name", "R");
        assertThat(mpaList.get(4)).hasFieldOrPropertyWithValue("id", 5);
        assertThat(mpaList.get(4)).hasFieldOrPropertyWithValue("name", "NC-17");
    }

    @Test
    void hasToGetMpaById() {
        assertThat(mpaStorage.getMpaById(1)).hasFieldOrPropertyWithValue("id", 1);
        assertThat(mpaStorage.getMpaById(1)).hasFieldOrPropertyWithValue("name", "G");
        assertThat(mpaStorage.getMpaById(5)).hasFieldOrPropertyWithValue("id", 5);
        assertThat(mpaStorage.getMpaById(5)).hasFieldOrPropertyWithValue("name", "NC-17");
    }
}