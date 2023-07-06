package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserIdValidator implements ConstraintValidator<ValidUserId, Integer> {


    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
        return id == 0 || UserController.getUsers().containsKey(id);
    }
}
