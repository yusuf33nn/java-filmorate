package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.experimental.UtilityClass;
import ru.yandex.practicum.filmorate.model.dto.response.UserEventFeedResponseDto;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;

import java.time.ZoneId;

@UtilityClass
public class UserEventFeedMapper {

    public UserEventFeedResponseDto toDto(UserEventFeed e) {
        var timestamp = e.getTimestamp()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        return UserEventFeedResponseDto.builder()
                .eventId(e.getEventId())
                .userId(e.getUserId())
                .entityId(e.getEntityId())
                .eventType(e.getEventType())
                .operation(e.getOperation())
                .timestamp(timestamp)
                .build();
    }
}
