package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.List;

@RequestMapping("/users")
public interface UserApi {

    @GetMapping
    ResponseEntity<List<User>> showAllUsers();

    @GetMapping("/{userId}")
    ResponseEntity<User> findUserById(@PathVariable Long userId);

    @PostMapping
    ResponseEntity<User> createUser(@Valid @RequestBody User user);

    @PutMapping
    ResponseEntity<User> updateUser(@Valid @RequestBody User user);
}
