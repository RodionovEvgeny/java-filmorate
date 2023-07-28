package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private final User user = new User("email@email.com",
            "login", LocalDate.now().minusYears(20), 0, "Name1");
    private final User noNameUser = new User("email@email.com",
            "login", LocalDate.now().minusYears(20), 0, "");
    private final User updatedUser = new User("email@email.com",
            "login", LocalDate.now().minusYears(20), 1, "New Name");
    private final User invalidEmailUser = new User("@email.com",
            "login", LocalDate.now().minusYears(20), 0, "Name");
    private final User invalidBirthDateUser = new User("email@email.com",
            "login", LocalDate.now().plusYears(20), 0, "Name");
    private final User invalidIdUser = new User("email@email.com",
            "login", LocalDate.now().minusYears(20), 900, "Name");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;

    @AfterEach
    void setup() {
        userController.deleteAllUsers();
    }

    @Test
    void hasToAddValidUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
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
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
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
                .andExpect(jsonPath("$[1].name").value("login"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void hasToIgnoreInvalidEmailUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(invalidEmailUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void hasToIgnoreInvalidBirthDateUser() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content(objectMapper.writeValueAsString(invalidBirthDateUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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
        mockMvc.perform(
                        get("/users")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}