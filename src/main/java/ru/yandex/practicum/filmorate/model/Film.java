package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.*;

@Data
public class Film {
    private static final LocalDate date = LocalDate.of(1895, 12, 28);
    private Instant datedate = date.atStartOfDay(ZoneId.systemDefault()).toInstant();

    private int id;
@NotBlank
    private final String name;
@Size(min = 0, max = 200)
    private final String description;

    private LocalDate releaseDate;

    @Positive
    private final int duration;
}
