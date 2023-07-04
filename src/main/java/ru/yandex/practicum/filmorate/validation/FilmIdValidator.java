package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmIdValidator implements ConstraintValidator<ValidFilmId, Integer> {


    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return id == 0 || FilmController.films.containsKey(id);
    }
}
