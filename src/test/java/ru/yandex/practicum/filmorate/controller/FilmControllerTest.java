package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        FilmController.deleteAllFilms();
    }

    @Test
    void hasToAddValidFilm() throws Exception {
        Film film = new Film("Name", "The best film ever.", 15, Film.FIRST_FILM_DATE.plusDays(15));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(1, FilmController.getFilms().size());
    }

    @Test
    void hasToUpdateValidFilm() throws Exception {
        Film film = new Film("Name", "The best film ever.", 15, Film.FIRST_FILM_DATE.plusDays(15));
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON));
        Film film2 = new Film("Name", "The best of the best film ever.", 15, Film.FIRST_FILM_DATE.plusDays(15));
        film2.setId(1);
        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(film2))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(1, FilmController.getFilms().size());
    }

    @Test
    void hasToReturnAllFilms() throws Exception { // TODO посмотреть как ответ от сервака обработать в моке. Надо чекнуть список там
        Film film = new Film("Name", "The best film ever.", 15, Film.FIRST_FILM_DATE.plusDays(15));
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film))
                        .contentType(MediaType.APPLICATION_JSON));
        Film film2 = new Film("Name2", "The best of the best film ever.", 15, Film.FIRST_FILM_DATE.plusDays(15));
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film2))
                        .contentType(MediaType.APPLICATION_JSON));

        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(2, FilmController.getFilms().size());
    }


    @Test
    void hasToIgnoreInvalidDescriptionFilm() throws Exception {
        Film film = new Film("Name", "The best film ever.The best film ever." +
                "The best film ever.The best film ever.The best film ever.The best film ever." +
                "The best film ever.The best film ever.The best film ever.The best film ever." +
                "The best film ever.The best film ever.The best film ever.The best film ever.",
                15, Film.FIRST_FILM_DATE.plusDays(15));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidNameFilm() throws Exception {
        Film film = new Film("", "The best film ever.The best film ever.",
                15, Film.FIRST_FILM_DATE.plusDays(15));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidDurationFilm() throws Exception {
        Film film = new Film("Name", "The best film ever.The best film ever.",
                0, Film.FIRST_FILM_DATE.plusDays(15));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidReleaseDateFilm() throws Exception {
        Film film = new Film("Name", "The best film ever.The best film ever.",
                15, Film.FIRST_FILM_DATE.minusDays(15));

        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }
}