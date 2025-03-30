package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_WithBlankName_ShouldReplaceNameWithLogin() throws Exception {

        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testlogin");
        user.setName(""); // пустое
        user.setBirthday(LocalDate.of(1990, 1, 1));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        String allUsersJson = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> users = objectMapper.readValue(allUsersJson, new TypeReference<>() {
        });
        assertEquals(1, users.size());
        assertEquals("testlogin", users.get(0).getName(), "Name должно замениться на login, если было пустое");
    }

    @Test
    void createUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setEmail("invalid-email"); // не соответствует формату email
        user.setLogin("login123");
        user.setName("Some Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        String userJson = objectMapper.writeValueAsString(user);


        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_WithBirthdayInFuture_ShouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setLogin("login123");
        user.setName("Name");
        user.setBirthday(LocalDate.now().plusDays(1)); // Будущее время

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithIdZero() throws Exception {
        User user = new User();
        user.setId(0L);
        user.setEmail("user@mail.com");
        user.setLogin("user123");
        user.setName("Invalid ID");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_WithIdNull() throws Exception {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.com");
        user.setLogin("user123");
        user.setName("Invalid ID");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }
}
