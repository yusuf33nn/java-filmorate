package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.dto.response.UserResponseDto;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.api.FriendsService;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.FriendsStorage;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFriendsService implements FriendsService {

    private final UserService userService;
    @Qualifier(value = "friendsDbStorage")
    private final FriendsStorage friendsStorage;
    private final UserMapper userMapper;

    @Override
    public Set<UserResponseDto> retrieveUsersFriends(Long userId) {
        userService.findUserById(userId);
        Set<User> userFriends = friendsStorage.retrieveUsersFriends(userId);
        if (CollectionUtils.isEmpty(userFriends)) {
            return Collections.emptySet();
        }
        return userFriends.stream().map(userMapper::toDto).collect(Collectors.toSet());
    }

    @Override
    public Set<UserResponseDto> showCommonFriends(Long userId, Long otherId) {
        userService.findUserById(userId);
        Set<User> userFriends = friendsStorage.retrieveUsersFriends(userId);
        if (userFriends.isEmpty()) {
            return Collections.emptySet();
        }

        userService.findUserById(otherId);
        Set<User> otherUserFriends = friendsStorage.retrieveUsersFriends(otherId);
        if (otherUserFriends.isEmpty()) {
            return Collections.emptySet();
        }
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void addToFriends(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);
        var insertedRows = friendsStorage.addToFriends(userId, friendId);
        if (insertedRows != 1) {
            throw new RuntimeException("Error while adding new friendship");
        }
    }

    @Override
    public void deleteFromFriends(Long userId, Long friendId) {
        userService.findUserById(userId);
        userService.findUserById(friendId);

        friendsStorage.deleteFromFriends(userId, friendId);
    }
}
