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

    private int id;
    @NotBlank
    private final String name;
    @Size(min = 0, max = 200)
    private final String description;
    @Positive
    private final int duration;
    @ValidDateFilm
    private LocalDate releaseDate;
}
