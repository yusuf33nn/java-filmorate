package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.api.UserStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
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
                DataAccessUtils.singleResult(
                        jdbcTemplate.query("select * from users where id = ?", userRowMapper, userId)
                )
        );
    }

    @Override
    public User saveUser(User user) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        GeneratedKeyHolder kh = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());          // «login» здесь — обычная колонка
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, kh);
        var generatedId = Optional.ofNullable(kh.getKey())
                .map(Number::longValue)
                .orElseThrow(() -> new RuntimeException("Id is not created"));
        user.setId(generatedId);
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
