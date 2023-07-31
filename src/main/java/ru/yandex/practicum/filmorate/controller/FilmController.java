package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
public class FilmController {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping("/films")
    private Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    private Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films")
    private Set<Film> getFilms(@PathVariable(name = "id", required = false) Integer id) {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    private Film getFilm(@PathVariable(name = "id") Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }
}
