package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.api.UserStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public List<User> showAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", userRowMapper);
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper, userId)
        );
    }

    @Override
    public User saveUser(User user) {
        var sql = """
                INSERT INTO users (email, login, name, birthday)
                VALUES (?, ?, ?, ?)
                RETURNING id
                """;
        Long userId = jdbcTemplate.queryForObject(sql, Long.class,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(userId);
        return user;
    }

    @Override
    public int updateUser(User user) {
        var sql = """
                UPDATE users
                   SET email    = ?,
                       login    = ?,
                       name     = ?,
                       birthday = ?
                 WHERE id       = ?
                """;
        return jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public void deleteUser(Long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }
}
