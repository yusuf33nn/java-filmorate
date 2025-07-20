package ru.yandex.practicum.filmorate.mapper.row;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.dto.response.EventType;
import ru.yandex.practicum.filmorate.model.dto.response.Operation;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserEventFeedRowMapper implements RowMapper<UserEventFeed> {
    @Override
    public UserEventFeed mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEventFeed.builder()
                .eventId(rs.getLong("event_id"))
                .userId(rs.getLong("user_id"))
                .eventType(EventType.valueOf(rs.getString("event_type")))
                .operation(Operation.valueOf(rs.getString("operation")))
                .entityId(rs.getLong("entity_id"))
                .timestamp(rs.getDate("timestamp").toLocalDate())
                .build();
    }
}
