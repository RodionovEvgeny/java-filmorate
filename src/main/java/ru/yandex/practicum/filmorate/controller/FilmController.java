package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashSet;
import java.util.Set;

@RestController
public class FilmController {
    private Set<Film> films = new HashSet<>();

    @PostMapping("/film")
    private Film addFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @PutMapping("/film")
    private Film updateFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }

    @GetMapping("/films")
    private Set<Film> getAllFilms() {
        return films;
    }

}
