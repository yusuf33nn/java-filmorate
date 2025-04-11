package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public List<User> showAllUsers() {
        return users.values().stream().toList();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public User saveUser(User user) {
        return users.put(user.getId(), user);
    }
}
