package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Request Body: {}", user);
        Long filmId = getNextId();
        user.setId(filmId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return users.put(filmId, user);
    }

    @GetMapping
    public List<User> showAllMovies() {
        return users.values().stream().toList();
    }

    @PutMapping
    public User updateMovie(@Valid @RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            log.error("User id cannot be null or zero for update operation");
        }
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.values()
                .stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
