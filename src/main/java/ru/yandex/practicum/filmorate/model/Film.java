package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidDateFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    public static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    @NotBlank(message = "Имя фильма не заполнено.")
    private final String name;
    @Size(min = 0, max = 200, message = "Длина описания должна быть от 0 до 200 символов.")
    private final String description;
    @Positive(message = "Продолжительность не может быть отрицательной.")
    private final int duration;
    @ValidDateFilm(message = "Дата релиза не может быть раньше создания первого фильма.")
    private final LocalDate releaseDate;
    // @ValidFilmId(message = "Фильм с данным id не найден.")
    private int id;
}
