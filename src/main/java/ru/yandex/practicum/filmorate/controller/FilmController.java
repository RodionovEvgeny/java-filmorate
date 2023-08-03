package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping
    private Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    private void addLike(@PathVariable Map<String, String> ids) {
        filmService.addLike(ids.get("id"), ids.get("userId"));
    }

    @DeleteMapping("/{id}/like/{userId}")
    private void deleteLike(@PathVariable Map<String, String> ids) {
        filmService.deleteLike(ids.get("id"), ids.get("userId"));
    }

    @GetMapping
    private Set<Film> getFilms(@PathVariable(name = "id", required = false) Integer id) {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/popular")
    private List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{id}")
    private Film getFilm(@PathVariable(name = "id") Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }
}
