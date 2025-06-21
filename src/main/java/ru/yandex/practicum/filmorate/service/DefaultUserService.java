package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.api.UserService;
import ru.yandex.practicum.filmorate.storage.api.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    @Qualifier(value = "userDbStorage")
    private final UserStorage userStorage;

    @Override
    public List<User> showAllUsers() {
        return userStorage.showAllUsers();
    }

    @Override
    public User findUserById(Long userId) {
        return userStorage.findUserById(userId)
                .orElseThrow(() -> {
                    var errorMessage = "User with ID: '%d' is not found".formatted(userId);
                    log.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });
    }


    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();
        if (userId == null || userId == 0) {
            var errorMessage = "User id cannot be null or zero for update operation";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        findUserById(userId);
        userStorage.updateUser(user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }
}
