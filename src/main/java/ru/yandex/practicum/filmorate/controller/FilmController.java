package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    private Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    private Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    private void addLike(@PathVariable(name = "id") Integer id,
                         @PathVariable(name = "userId") Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    private void deleteLike(@PathVariable(name = "id") Integer id,
                            @PathVariable(name = "userId") Integer userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping
    private List<Film> getFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    private List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/{id}")
    private Film getFilm(@PathVariable(name = "id") Integer id) {
        return filmService.getFilmById(id);
    }

    @DeleteMapping
    public void deleteAllFilms() {
        filmService.deleteAllFilms();
    }
}
