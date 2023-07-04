package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static int nextVacantId = 1;
    public static final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        if (film.getId() == 0) film.setId(nextVacantId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    private Film updateFilm(@Valid @RequestBody Film film) {
       /* if (!films.containsKey(film.getId())) {
            throw new ValidationException("Данного фильма не существует.");
        }*/
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    private Set<Film> getAllFilms() {
        return new HashSet<>(films.values());
    }
}
