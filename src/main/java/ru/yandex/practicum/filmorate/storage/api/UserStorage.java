package ru.yandex.practicum.filmorate.storage.api;

import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> showAllUsers();

    Optional<User> findUserById(Long userId);

    User saveUser(User user);

    int updateUser(User user);

    void deleteUser(Long userId);
}
