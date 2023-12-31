package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidDateFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Имя фильма не заполнено.")
    private final String name;
    @NotNull(message = "У фильма должно быть описание.")
    @Size(min = 0, max = 200, message = "Длина описания должна быть от 0 до 200 символов.")
    private final String description;
    @Positive(message = "Продолжительность не может быть отрицательной.")
    private final int duration;
    @ValidDateFilm(message = "Дата релиза не может быть раньше создания первого фильма.")
    private final LocalDate releaseDate;
    private LinkedHashSet<Genre> genres;
    @NotNull(message = "У фильма должен быть рейтинг.")
    private Mpa mpa;
}
