package ru.yandex.practicum.filmorate.storage.db_storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.row.UserEventFeedRowMapper;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;
import ru.yandex.practicum.filmorate.storage.api.UserEventFeedStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserEventFeedDbStorage implements UserEventFeedStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserEventFeedRowMapper rowMapper;

    @Override
    public void saveEvent(UserEventFeed userEventFeed) {
        var sql = """
                INSERT INTO user_event_feed
                      (user_id, event_type, operation, entity_id, timestamp)
                VALUES (?, ?, ?, ?, ?)
                """;

        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, userEventFeed.getUserId());
            ps.setString(2, userEventFeed.getEventType().name());
            ps.setString(3, userEventFeed.getOperation().name());
            ps.setLong(4, userEventFeed.getEntityId());
            ps.setObject(5, userEventFeed.getTimestamp());
            return ps;
        }, keyHolder);
    }

    @Override
    public List<UserEventFeed> getLastUserEvents(Long userId) {
        var sql = """
            SELECT event_id,
                   user_id,
                   event_type,
                   operation,
                   entity_id,
                   timestamp
            FROM   user_event_feed
            WHERE  user_id = ?
            ORDER  BY timestamp DESC
            """;
        return jdbcTemplate.query(sql, rowMapper, userId);
    }
}
