package ru.yandex.practicum.filmorate.controller.api;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;

import java.util.List;

@RequestMapping("/users")
public interface UserApi {

    @GetMapping
    ResponseEntity<List<UserResponseDto>> showAllUsers();

    @GetMapping("/{userId}")
    ResponseEntity<UserResponseDto> findUserById(@PathVariable Long userId);

    @PostMapping
    ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto user);

    @PutMapping
    ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto user);
}
