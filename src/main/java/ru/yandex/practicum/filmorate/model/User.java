package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@Data
public class User {
    private static int nextId = 1;
    @EqualsAndHashCode.Include
    private int id;
    @Email
    private final String email;
    @NotEmpty(message = "Empty login")
    private final  String login;

    private String name;
    @Past
    private final LocalDate birthday;
}
