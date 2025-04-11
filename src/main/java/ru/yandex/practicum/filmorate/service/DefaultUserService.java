package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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
    public Set<User> retrieveUsersFriends(Long userId) {
        User user = findUserById(userId);
        Set<Long> userFriends = user.getFriends();
        if (userFriends == null || userFriends.isEmpty()) {
            return Collections.emptySet();
        }
        return user.getFriends().stream()
                .map(userStorage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> showCommonFriends(Long userId, Long otherId) {
        Set<Long> userFriends = findUserById(userId).getFriends();
        if (userFriends == null) {
            return Collections.emptySet();
        }
        Set<Long> otherUserFriends = findUserById(otherId).getFriends();
        if (otherUserFriends == null) {
            return Collections.emptySet();
        }
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
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
            var errorMessage = "User id cannot be null or zero for update operation";
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
        findUserById(userId);
        userStorage.saveUser(user);
        return user;
    }

    @Override
    public User addToFriends(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        return user;
    }

    @Override
    public User deleteFromFriends(Long userId, Long friendId) {
        User user = findUserById(userId);
        User friend = findUserById(friendId);


        user.getFriends().remove(friendId);


        friend.getFriends().remove(userId);
        return user;
    }
}
