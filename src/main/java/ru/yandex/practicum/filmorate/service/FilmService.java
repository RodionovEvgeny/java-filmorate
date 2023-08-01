package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(String filmId, String userId) { // TODO валидация айдишек
        Film film = filmStorage.getFilmById(Integer.valueOf(filmId));
        User user = userStorage.getUserById(Integer.valueOf(userId));
        film.getLikes().add(user.getId());
    }

    public void deleteLike(String filmId, String userId) {
        Film film = filmStorage.getFilmById(Integer.valueOf(filmId));
        User user = userStorage.getUserById(Integer.valueOf(userId));
        film.getLikes().remove(user.getId());
    }

    public List<Film> getTopFilms(Integer count) {
        if (count == null) count = 10;
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
