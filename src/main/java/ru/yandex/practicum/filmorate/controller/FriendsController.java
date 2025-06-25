package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.api.FriendsApi;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.service.api.FriendsService;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendsController implements FriendsApi {

    private final FriendsService friendsService;

    @Override
    public ResponseEntity<Set<UserResponseDto>> retrieveUserFriends(Long userId) {
        return ResponseEntity.ok(friendsService.retrieveUsersFriends(userId));
    }

    @Override
    public ResponseEntity<Set<UserResponseDto>> showCommonFriends(Long userId, Long otherId) {
        return ResponseEntity.ok(friendsService.showCommonFriends(userId, otherId));
    }

    @Override
    public ResponseEntity<Void> addToFriends(Long userId, Long friendId) {
        friendsService.addToFriends(userId, friendId);
        return ResponseEntity.status(OK).build();
    }

    @Override
    public ResponseEntity<Void> deleteFromFriends(Long userId, Long friendId) {
        friendsService.deleteFromFriends(userId, friendId);
        return ResponseEntity.status(OK).build();
    }
}
