package ru.yandex.practicum.filmorate.service.api;

import ru.yandex.practicum.filmorate.model.dto.response.UserEventFeedResponseDto;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;

import java.util.List;

public interface UserEventFeedService {

    void saveEvent(UserEventFeed userEventFeed);

    List<UserEventFeedResponseDto> getLastUserEvents(Long userId);
}
