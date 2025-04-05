package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.UserApi;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
public class UserController implements UserApi {

    private final AtomicLong userId = new AtomicLong(0L);
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Request Body: {}", user);
        Long filmId = userId.incrementAndGet();
        user.setId(filmId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(filmId, user);
        return ResponseEntity.status(CREATED).body(user);
    }

    @Override
    public ResponseEntity<List<User>> showAllUsers() {
        return ResponseEntity.ok(users.values().stream().toList());
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        Long userId = user.getId();
        if (userId == null || userId == 0) {
            log.error("User id cannot be null or zero for update operation");
            return ResponseEntity.badRequest().build();
        }
        if (users.get(userId) == null) {
            log.error("You cannot update user, if the user doesn't exist");
            return ResponseEntity.internalServerError().body(user);
        }
        users.put(userId, user);
        return ResponseEntity.ok(user);
    }
}
