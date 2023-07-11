package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidUserId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {

    // @ValidUserId(message = "Пользователь с данным id не найден.")
    private int id;
    @Email(message = "Введен некорректный email.")
    private final String email;
    @NotEmpty(message = "Введен пустой логин.")
    private final  String login;
    private String name;
    @Past(message = "Введена недопустимая дата рождения.")
    private final LocalDate birthday;
}
