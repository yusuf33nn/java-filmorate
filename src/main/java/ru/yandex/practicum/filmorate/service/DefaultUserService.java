package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final AtomicLong userId = new AtomicLong(0L);

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
    public Set<Long> retrieveUsersFriends(Long userId) {
        User user = findUserById(userId);
        return user.getFriends();
    }

    @Override
    public Set<Long> showCommonFriends(Long userId, Long otherId) {
        User user = findUserById(userId);
        User otherUser = findUserById(otherId);
        return null;
    }

    @Override
    public User createUser(User user) {
        Long filmId = userId.incrementAndGet();
        user.setId(filmId);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.saveUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();
        if (userId == null || userId == 0) {
            log.error("User id cannot be null or zero for update operation");
            throw new RuntimeException();
        }
        findUserById(userId);
        userStorage.saveUser(user);
        return user;
    }

    @Override
    public User addToFriends(Long userId, Long friendId) {
        return null;
    }

    @Override
    public User deleteFromFriends(Long userId, Long friendId) {
        return null;
    }
}
