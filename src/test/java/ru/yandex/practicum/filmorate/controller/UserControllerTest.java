package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.api.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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

        user.setId(1L);
        user.setName("testlogin");
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userService.showAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("testlogin"))
                .andExpect(jsonPath("$[0].email").value("test@mail.com"));
    }

    @Test
    void createUser_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        User user = new User();
        user.setEmail("invalid-email"); // не соответствует формату email
        user.setLogin("login123");
        user.setName("Some Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        String userJson = objectMapper.writeValueAsString(user);
        when(userService.createUser(any(User.class))).thenThrow(new ValidationException());

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
        when(userService.createUser(any(User.class))).thenThrow(new ValidationException());

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
        when(userService.updateUser(any(User.class))).thenThrow(new ValidationException());

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
        when(userService.updateUser(any(User.class))).thenThrow(new ValidationException());
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest());
    }
}
