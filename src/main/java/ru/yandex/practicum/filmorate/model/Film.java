package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.filmorate.validation.ValidDateFilm;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @NotBlank(message = "Имя фильма не заполнено.")
    private final String name;
    @Size(min = 0, max = 200, message = "Длина описания должна быть от 0 до 200 символов.")
    private final String description;
    @Positive(message = "Продолжительность не может быть отрицательной.")
    private final int duration;
    @ValidDateFilm(message = "Дата релиза не может быть раньше создания первого фильма.")
    private final LocalDate releaseDate;
    private int id;
    private Set<Integer> likes = new HashSet<>();
    private Set<FilmGenres> genre;
    private FilmRating filmRating;

    public int getFilmRatingId() {
        if (filmRating == null) return 6;
        switch (filmRating) {
            case G:
                return 1;
            case PG:
                return 2;
            case PG13:
                return 3;
            case R:
                return 4;
            case NC17:
                return 5;
            default:
                return 6;
        }
    }

    public static FilmRating getFilmRatingById(int id){
        switch (id) {
            case 1:
                return FilmRating.G;
            case 2:
                return FilmRating.PG;
            case 3:
                return FilmRating.PG13;
            case 4:
                return FilmRating.R;
            case 5:
                return FilmRating.NC17;
            default:
                return FilmRating.NOT_STATED;
        }
    }

   /* public Film(String name, String description, int duration, LocalDate releaseDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    public Film(String name, String description, int duration, LocalDate releaseDate, FilmRating filmRating) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.filmRating = filmRating;
    }*/
}
