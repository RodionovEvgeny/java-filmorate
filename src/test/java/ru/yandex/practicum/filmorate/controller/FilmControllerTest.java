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
    private final Film film1 = new Film("Name 1", "Description 1.",
            15, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film updatedFilm = new Film("New Name", "New Description.",
            15, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film film2 = new Film("Name 2", "Description 2.",
            15, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film invalidDescriptionFilm = new Film("Name",
            "Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. ",
            15, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film invalidNameFilm = new Film("", "Description.",
            15, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film invalidDurationFilm = new Film("Name", "Description.",
            0, Film.FIRST_FILM_DATE.plusDays(15));
    private final Film invalidReleaseDateFilm = new Film("Name", "Description.",
            15, Film.FIRST_FILM_DATE.minusDays(15));

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
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(1, FilmController.getFilms().size());
    }

    @Test
    void hasToUpdateValidFilm() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON));
        updatedFilm.setId(1);
        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(1, FilmController.getFilms().size());
    }

    @Test
    void hasToReturnAllFilms() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON));
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
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidDescriptionFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidNameFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidNameFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidDurationFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidDurationFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }

    @Test
    void hasToIgnoreInvalidReleaseDateFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidReleaseDateFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, FilmController.getFilms().size());
    }
}