package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private final User user = new User(0, "email@email.com",
            "login", "Name1", LocalDate.now().minusYears(20));
    private final User noNameUser = new User(0, "email@email.com",
            "login", "", LocalDate.now().minusYears(20));
    private final User updatedUser = new User(1, "email@email.com",
            "login", "New Name", LocalDate.now().minusYears(20));
    private final User invalidEmailUser = new User(0, "@email.com",
            "login", "Name", LocalDate.now().minusYears(20));
    private final User invalidBirthDateUser = new User(0, "email@email.com",
            "login", "Name", LocalDate.now().plusYears(20));
    private final User invalidIdUser = new User(900, "email@email.com",
            "login", "Name", LocalDate.now().minusYears(20));
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void setup() {
        UserController.deleteAllUsers();
    }

    @Test
    void hasToAddValidUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        Assertions.assertEquals(1, UserController.getUsers().size());
    }

    @Test
    void hasToUpdateValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
        Assertions.assertEquals(1, UserController.getUsers().size());
    }

    @Test
    void hasToReturnAllUsers() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(noNameUser))
                        .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name").value("login"));
        Assertions.assertEquals(2, UserController.getUsers().size());
    }

    @Test
    void hasToIgnoreInvalidEmailUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(invalidEmailUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, UserController.getUsers().size());
    }

    @Test
    void hasToIgnoreInvalidBirthDateUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(invalidBirthDateUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(0, UserController.getUsers().size());
    }

    @Test
    void hasToIgnoreInvalidIdUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON));
        mockMvc.perform(
                        put("/users")
                                .content(objectMapper.writeValueAsString(invalidIdUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
        Assertions.assertEquals(1, UserController.getUsers().size());
    }
}