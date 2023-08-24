package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController { // TODO логирование сделать человеческое везде
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    private List<Genre> getAllUsers() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    private Genre getUser(@PathVariable(name = "id") Integer id) { // TODO посмотреть, мб можно дефолтное значение для вариаблы сделать
        return genreService.getGenreById(id);
    }
}
