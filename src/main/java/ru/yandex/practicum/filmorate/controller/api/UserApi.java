package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@RequestMapping("/users")
public interface UserApi {

    @GetMapping
    ResponseEntity<List<User>> showAllUsers();

    @GetMapping("/{id}")
    ResponseEntity<User> findUserById(@PathVariable Long id);

    @GetMapping("/{id}/friends")
    ResponseEntity<Set<Long>> retrieveUsersFriends(@PathVariable Long id);

    @GetMapping("/{id}/friends/common/{otherId}")
    ResponseEntity<Set<Long>> showCommonFriends(@PathVariable Long id, @PathVariable Long otherId);

    @PostMapping
    ResponseEntity<User> createUser(@Valid @RequestBody User user);

    @PutMapping
    ResponseEntity<User> updateUser(@Valid @RequestBody User user);

    @PutMapping("/{id}/friends/{friendId}")
    ResponseEntity<User> addToFriends(@PathVariable Long id, @PathVariable Long friendId);

    @DeleteMapping("/{id}/friends/{friendId}")
    ResponseEntity<Void> deleteFromFriends(@PathVariable Long id, @PathVariable Long friendId);
}
