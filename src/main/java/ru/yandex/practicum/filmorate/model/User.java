package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Email(message = "Введен некорректный email.")
    private final String email;
    @NotEmpty(message = "Введен пустой логин.")
    private final String login;
    @Past(message = "Введена недопустимая дата рождения.")
    private final LocalDate birthday;
    private int id;
    private String name;
    private Set<Integer> friends = new HashSet<>();

    public User(String email, String login, LocalDate birthday, int id, String name) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.id = id;
        this.name = name;
    }
}
