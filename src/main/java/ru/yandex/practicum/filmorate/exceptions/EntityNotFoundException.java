package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    private final String notFoundClassName;

    public EntityNotFoundException(String message, String notFoundClassName) {
        super(message);
        this.notFoundClassName = notFoundClassName;
    }

    public String getNotFoundClassName() {
        return notFoundClassName;
    }
}
