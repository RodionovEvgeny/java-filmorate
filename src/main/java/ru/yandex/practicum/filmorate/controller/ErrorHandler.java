package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.ExceptionDTO;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleNotFoundException(final EntityNotFoundException e) {
        log.warn(String.format("Не найден объект класса %s.", e.getNotFoundClassName()));
        log.warn(e.getMessage());
        return new ExceptionDTO(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleValidationException(final ValidationException e) {
        return new ExceptionDTO(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return new ExceptionDTO(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleConstraintViolationException(final ConstraintViolationException e) {
        log.warn(e.getMessage());
        return new ExceptionDTO(e.getMessage());
    }
}
