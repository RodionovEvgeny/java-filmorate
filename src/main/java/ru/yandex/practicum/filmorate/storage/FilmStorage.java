package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Set<Film> getAllFilms();

    void deleteAllFilms();

    Film getFilmById(Integer id);

    void addLike(Integer filmId, Integer userId);
    void deleteLike(Integer filmId, Integer userId);
    public List<Film> getTopFilms(Integer count);

}
