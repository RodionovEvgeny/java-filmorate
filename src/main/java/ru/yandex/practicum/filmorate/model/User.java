package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotEmpty(message = "Введен пустой email.")
    @Email(message = "Введен некорректный email.")
    private final String email;
    @NotEmpty(message = "Введен пустой логин.")
    @Pattern(regexp = "^[a-zA-Z0-9]{1,30}$",
            message = "Логин должен состоять только из цифр и букв и быть длиной 1 - 30 символов")
    private final String login;
    @NotNull(message = "Введена пустая дата рождения.")
    @PastOrPresent(message = "Введена недопустимая дата рождения.")
    private final LocalDate birthday;
    private String name;
}
