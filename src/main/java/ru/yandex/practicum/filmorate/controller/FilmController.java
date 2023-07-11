package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private static final Map<Integer, Film> films = new HashMap<>();
    private static int nextVacantId = 1;

    public static Map<Integer, Film> getFilms() {
        return films;
    }

    public static void deleteAllFilms() {
        films.clear();
        nextVacantId = 1;
    }

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        if (film.getId() == 0) film.setId(nextVacantId++);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен. Текущее количество фильмов {}", films.size());
        return film;
    }

    @PutMapping
    private Film updateFilm(@Valid @RequestBody Film film) {
        if (!(film.getId() == 0 || FilmController.getFilms().containsKey(film.getId()))) {
            log.warn("Фильм с id = {} не найден.", film.getId());
            throw new ValidationException("Фильм с такиим id не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Данные фильма {} обновлены.", film.getName());
        return film;
    }

    @GetMapping
    private Set<Film> getAllFilms() {
        log.debug("Список всех фильмов отправлен.");
        return new HashSet<>(films.values());
    }
}
