package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import ru.yandex.practicum.filmorate.validation.ValidUserId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
@Data
@AllArgsConstructor

public class User {
    @ValidUserId
    private int id;
    @Email
    private final String email;
    @NotEmpty(message = "Empty login")
    private final  String login;

    private String name;
    @Past
    private final LocalDate birthday;
}
