package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }

    public void deleteLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    public Film[] getTop10Films() {
        return (Film[]) filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(f -> f.getLikes().size()))
                .limit(10)
                .toArray();
    }
}
