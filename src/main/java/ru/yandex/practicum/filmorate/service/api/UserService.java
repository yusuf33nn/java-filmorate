package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> showAllUsers();

    User findUserById(Long userId);

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}
