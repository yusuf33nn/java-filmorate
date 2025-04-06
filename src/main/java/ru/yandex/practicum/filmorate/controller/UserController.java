package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.UserApi;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<User>> showAllUsers() {
        return ResponseEntity.ok(userService.showAllUsers());
    }

    @Override
    public ResponseEntity<User> findUserById(Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Override
    public ResponseEntity<Set<Long>> retrieveUsersFriends(Long id) {
        return ResponseEntity.ok(userService.retrieveUsersFriends(id));
    }

    @Override
    public ResponseEntity<Set<Long>> showCommonFriends(Long id, Long otherId) {
        return null;
    }

    @Override
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Request Body: {}", user);
        return ResponseEntity.status(CREATED).body(userService.createUser(user));
    }

    @Override
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Request Body: {}", user);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @Override
    public ResponseEntity<User> addToFriends(Long id, Long friendId) {
        return ResponseEntity.ok(userService.addToFriends(id, friendId));
    }

    @Override
    public ResponseEntity<Void> deleteFromFriends(Long id, Long friendId) {
        userService.deleteFromFriends(id, friendId);
        return ResponseEntity.status(OK).build();
    }
}
