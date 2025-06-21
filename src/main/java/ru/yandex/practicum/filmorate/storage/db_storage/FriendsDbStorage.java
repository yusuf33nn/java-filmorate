package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.api.FriendsStorage;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public Set<User> retrieveUsersFriends(Long userId) {
        return Set.of();
    }

    @Override
    public Set<User> showCommonFriends(Long userId, Long otherId) {
        return Set.of();
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
