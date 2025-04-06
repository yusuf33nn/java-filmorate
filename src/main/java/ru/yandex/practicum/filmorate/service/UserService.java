package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    List<User> showAllUsers();

    User findUserById(Long userId);

    Set<User> retrieveUsersFriends(Long userId);

    Set<User> showCommonFriends(Long userId, Long otherId);

    User createUser(User user);

    User updateUser(User user);

    User addToFriends(Long userId, Long friendId);

    User deleteFromFriends(Long userId, Long friendId);
}
