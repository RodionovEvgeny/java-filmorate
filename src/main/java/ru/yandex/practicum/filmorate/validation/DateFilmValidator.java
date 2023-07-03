package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateFilmValidator implements ConstraintValidator<ValidDateFilm, LocalDate> {


    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(Film.FIRST_FILM_DATE);
    }
}
