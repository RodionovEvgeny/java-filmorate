package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilmIdValidator implements ConstraintValidator<ValidFilmId, Integer> {
    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        if (!(id == 0 || FilmController.getFilms().containsKey(id))) {
            throw new ValidationException("Пользователь с такиим id не найден.");
        }
        return true;
    }
}
