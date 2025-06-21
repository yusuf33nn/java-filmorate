package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;

import java.util.Set;

public interface FriendsService {

    Set<UserResponseDto> retrieveUsersFriends(Long userId);

    Set<UserResponseDto> showCommonFriends(Long userId, Long otherId);

    void addToFriends(Long userId, Long friendId);

    void deleteFromFriends(Long userId, Long friendId);
}
