package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class FilmControllerTest {
    private final Film film1 = Film.builder()
            .name("Name 1")
            .description("Description 1.")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film updatedFilm = Film.builder()
            .name("New Name")
            .description("New Description.")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film film2 = Film.builder()
            .name("Name 2")
            .description("Description 2.")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film invalidDescriptionFilm = Film.builder()
            .name("Name")
            .description("Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. " +
                    "Invalid description. Invalid description. Invalid description. ")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film invalidNameFilm = Film.builder()
            .name("")
            .description("Description.")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film invalidDurationFilm = Film.builder()
            .name("Name")
            .description("Description.")
            .duration(0)
            .releaseDate(LocalDate.of(1895, 12, 28).plusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();
    private final Film invalidReleaseDateFilm = Film.builder()
            .name("Name")
            .description("Description.")
            .duration(15)
            .releaseDate(LocalDate.of(1895, 12, 28).minusDays(15))
            .mpa(new Mpa(1, null))
            .genres(List.of(new Genre(1, null)))
            .build();


    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;

    @BeforeEach
    void setup() {
        filmController.deleteAllFilms();

    }

    @Test
    void hasToAddValidFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void hasToUpdateValidFilm() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON));
        updatedFilm.setId(5);
        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void hasToIgnoreInvalidIdFilm() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(film1))
                        .contentType(MediaType.APPLICATION_JSON));
        updatedFilm.setId(99);
        mockMvc.perform(
                        put("/films")
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void hasToIgnoreInvalidDescriptionFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidDescriptionFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void hasToIgnoreInvalidNameFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidNameFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void hasToIgnoreInvalidDurationFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidDurationFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void hasToIgnoreInvalidReleaseDateFilm() throws Exception {
        mockMvc.perform(
                        post("/films")
                                .content(objectMapper.writeValueAsString(invalidReleaseDateFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/films")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}