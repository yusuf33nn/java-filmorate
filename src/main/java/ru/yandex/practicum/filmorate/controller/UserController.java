package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final AtomicLong userId = new AtomicLong(0L);
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Request Body: {}", user);
        Long filmId = userId.incrementAndGet();
        user.setId(filmId);
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return ResponseEntity.status(CREATED)
                .body(users.put(filmId, user));
    }

    @GetMapping
    public ResponseEntity<List<User>> showAllMovies() {
        return ResponseEntity.ok(users.values().stream().toList());
    }

    @PutMapping
    public ResponseEntity<User> updateMovie(@Valid @RequestBody User user) {
        if (user.getId() == null || user.getId() == 0) {
            log.error("User id cannot be null or zero for update operation");
            return ResponseEntity.badRequest().build();
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok(user);
    }
}
