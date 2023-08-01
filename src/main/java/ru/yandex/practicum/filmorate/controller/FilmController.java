package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    private Film addFilm(@Valid @RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    private Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    private void addLike(@PathVariable Map<String, String> ids) {
        filmService.addLike(ids.get("id"), ids.get("userId"));
    }
    @DeleteMapping("/films/{id}/like/{userId}")
    private void deleteLike(@PathVariable Map<String, String> ids) {
        filmService.deleteLike(ids.get("id"), ids.get("userId"));
    }

    @GetMapping("/films")
    private Set<Film> getFilms(@PathVariable(name = "id", required = false) Integer id) {
        return filmStorage.getAllFilms();
    }
    @GetMapping("/films/popular")
    private List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/films/{id}")
    private Film getFilm(@PathVariable(name = "id") Integer id) {
        return filmStorage.getFilmById(id);
    }

    public void deleteAllFilms() {
        filmStorage.deleteAllFilms();
    }
}
