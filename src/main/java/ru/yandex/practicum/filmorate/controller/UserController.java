package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.UserApi;
import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.service.api.UserService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<List<UserResponseDto>> showAllUsers() {
        return ResponseEntity.ok(userService.showAllUsers());
    }

    @Override
    public ResponseEntity<UserResponseDto> findUserById(Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @Override
    public ResponseEntity<UserResponseDto> createUser(UserRequestDto user) {
        log.info("Request Body: {}", user);
        return ResponseEntity.status(CREATED).body(userService.createUser(user));
    }

    @Override
    public ResponseEntity<UserResponseDto> updateUser(UserRequestDto user) {
        log.info("Request Body: {}", user);
        var response = userService.updateUser(user);
        return ResponseEntity.ok(response);
    }
}
