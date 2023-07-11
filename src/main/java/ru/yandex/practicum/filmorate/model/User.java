package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    @Email(message = "Введен некорректный email.")
    private final String email;
    @NotEmpty(message = "Введен пустой логин.")
    private final String login;
    @Past(message = "Введена недопустимая дата рождения.")
    private final LocalDate birthday;
    // @ValidUserId(message = "Пользователь с данным id не найден.")
    private int id;
    private String name;
}
