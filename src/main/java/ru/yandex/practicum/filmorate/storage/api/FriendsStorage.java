package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.Set;

public interface FriendsStorage {

    Set<User> retrieveUsersFriends(Long userId);

    Set<User> showCommonFriends(Long userId, Long otherId);

    int addToFriends(Long userId, Long friendId);

    void deleteFromFriends(Long userId, Long friendId);
}
