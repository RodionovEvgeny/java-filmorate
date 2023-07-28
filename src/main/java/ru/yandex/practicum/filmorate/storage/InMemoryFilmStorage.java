package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int nextVacantId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() == 0)
            film.setId(nextVacantId++); // TODO если передать фильм с айди то он обновит фильм а не добавит новый. Это баг или фича?
        films.put(film.getId(), film);
        log.debug("Фильм добавлен. Текущее количество фильмов {}", films.size());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!(film.getId() == 0 || films.containsKey(film.getId()))) {
            log.warn("Фильм с id = {} не найден.", film.getId());
            throw new ValidationException("Фильм с такиим id не найден.");
        }
        films.put(film.getId(), film);
        log.debug("Данные фильма {} обновлены.", film.getName());
        return film;
    }

    @Override
    public Set<Film> getAllFilms() {
        log.debug("Список всех фильмов отправлен.");
        return new HashSet<>(films.values());
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
        nextVacantId = 1;
    }
}
