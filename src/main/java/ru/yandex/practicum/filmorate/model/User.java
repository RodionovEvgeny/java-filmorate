package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
@Data
public class User {
    @EqualsAndHashCode.Include
    private final int id;
    private final String email;
    private final  String login;
    private final String name;
    private final LocalDate birthday;
}
