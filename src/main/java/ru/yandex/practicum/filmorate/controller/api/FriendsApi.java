package ru.yandex.practicum.filmorate.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;

import java.util.Set;

@RequestMapping(value = "/users/{userId}/friends")
public interface FriendsApi {

    @GetMapping
    ResponseEntity<Set<UserResponseDto>> retrieveUserFriends(@PathVariable Long userId);

    @GetMapping("/common/{otherId}")
    ResponseEntity<Set<UserResponseDto>> showCommonFriends(@PathVariable Long userId, @PathVariable Long otherId);

    @PutMapping("/{friendId}")
    ResponseEntity<Void> addToFriends(@PathVariable Long userId, @PathVariable Long friendId);

    @DeleteMapping("/{friendId}")
    ResponseEntity<Void> deleteFromFriends(@PathVariable Long userId, @PathVariable Long friendId);
}
