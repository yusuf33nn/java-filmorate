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
import ru.yandex.practicum.filmorate.model.dto.request.UserRequestDto;
import ru.yandex.practicum.filmorate.model.dto.response.FilmResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserEventFeedResponseDto;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.List;

@RequestMapping("/users")
public interface UserApi {

    @GetMapping
    ResponseEntity<List<UserResponseDto>> showAllUsers();

    @GetMapping("/{userId}")
    ResponseEntity<UserResponseDto> findUserById(@PathVariable("userId") Long userId);

    @GetMapping("/{id}/feed")
    ResponseEntity<List<UserEventFeedResponseDto>> getLastUserEvents(@PathVariable("id") Long userId);

    @PostMapping
    ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto user);

    @PutMapping
    ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserRequestDto user);

    @GetMapping("/{userId}/recommendations")
    ResponseEntity<List<FilmResponseDto>> getRecommendations(@PathVariable Long userId);

    @DeleteMapping("/{userId}")
    ResponseEntity<UserResponseDto> removeUserById(@PathVariable Long userId);
}
