package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.api.FriendsStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public Set<User> retrieveUsersFriends(Long userId) {
        var sql = "SELECT * " +
                    "FROM USERS " +
                   "WHERE ID in (" +
                        "SELECT RECEIVER_ID " +
                          "FROM FRIENDSHIP " +
                         "WHERE REQUESTER_ID = ? " +
                           "AND STATUS = 'CONFIRMED'" +
                ")";
        List<User> userFriends = jdbcTemplate.query(sql, userRowMapper, userId);
        return new LinkedHashSet<>(userFriends);
    }

    @Override
    public Set<User> showCommonFriends(Long userId, Long otherId) {
        return Set.of();
    }

    @Override
    public int addToFriends(Long userId, Long friendId) {
        try {
            var requesterSql = "insert into friendship (REQUESTER_ID, RECEIVER_ID, STATUS) values (?, ?, 'CONFIRMED')";
            jdbcTemplate.update(requesterSql, userId, friendId);
            var receiverSql = "insert into friendship (REQUESTER_ID, RECEIVER_ID, STATUS) values (?, ?, 'PENDING')";
            return jdbcTemplate.update(receiverSql, friendId, userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Cannot add friendship: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFromFriends(Long userId, Long friendId) {
        try {
            var requesterSql = "DELETE FROM friendship WHERE REQUESTER_ID = ? AND RECEIVER_ID = ?";
            jdbcTemplate.update(requesterSql, userId, friendId);
//            var receiverSql = "DELETE FROM friendship WHERE REQUESTER_ID = ? AND RECEIVER_ID = ?";
//            jdbcTemplate.update(receiverSql, friendId, userId);
        } catch (DataAccessException e) {
            throw new RuntimeException("Cannot remove friendship: " + e.getMessage(), e);
        }
    }
}
