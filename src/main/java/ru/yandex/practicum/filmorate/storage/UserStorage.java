package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> showAllUsers();

    Optional<User> findUserById(Long userId);

    User saveUser(User user);
}
