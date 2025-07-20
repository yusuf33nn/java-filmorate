package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.dto.UserEventFeedMapper;
import ru.yandex.practicum.filmorate.model.dto.response.UserEventFeedResponseDto;
import ru.yandex.practicum.filmorate.model.entity.UserEventFeed;
import ru.yandex.practicum.filmorate.service.api.UserEventFeedService;
import ru.yandex.practicum.filmorate.storage.db_storage.UserEventFeedDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultUserEventFeedService implements UserEventFeedService {

    private final UserEventFeedDbStorage userEventFeedDbStorage;

    @Override
    public void saveEvent(UserEventFeed userEventFeed) {
        userEventFeedDbStorage.saveEvent(userEventFeed);
    }

    @Override
    public List<UserEventFeedResponseDto> getLastUserEvents(Long userId) {
        List<UserEventFeedResponseDto> events = userEventFeedDbStorage.getLastUserEvents(userId)
                .stream()
                .map(UserEventFeedMapper::toDto)
                .toList();
        if (CollectionUtils.isEmpty(events)) {
            throw new NotFoundException("Лента для пользователя '%d' пуста".formatted(userId));
        }
        return events;
    }
}
