package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserIdValidator implements ConstraintValidator<ValidUserId, Integer> {
    @Override
    public boolean isValid(Integer id, ConstraintValidatorContext constraintValidatorContext) {
if (!(id == 0 || UserController.getUsers().containsKey(id)))
        throw new ValidationException("Фильм с такиим id не найден.");
        return true;
    }
}
